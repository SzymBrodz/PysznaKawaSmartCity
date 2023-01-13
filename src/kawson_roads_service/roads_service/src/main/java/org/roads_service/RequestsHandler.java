package org.roads_service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.apache.http.client.methods.HttpGet;

import java.util.Iterator;

import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class RequestsHandler {
    private final static Executor executor = Executors.newCachedThreadPool();
    private final static CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();

    public static String sendSyncGet(final String url) {
        return sendAsyncGet(List.of(url)).get(0);
    }

    public static List<String> sendAsyncGet(final List<String> urls){
        List<GetRequestTask> tasks = urls.stream().map(url ->  new GetRequestTask(url, executor)).collect(Collectors.toList());
        List<String> responses = new ArrayList<>();
        while(!tasks.isEmpty()) {
            for(Iterator<GetRequestTask> it = tasks.iterator(); it.hasNext();) {
                final GetRequestTask task = it.next();
                if(task.isDone()) {
                    responses.add(task.getResponse());
                    it.remove();
                }
            }
            //if(!tasks.isEmpty()) Thread.sleep(100); //avoid tight loop in "main" thread
        }
        return responses;
    }

    private static class GetRequestTask {
        private final FutureTask<String> task;

        public GetRequestTask(String url, Executor executor) {
            GetRequestWork work = new GetRequestWork(url);
            this.task = new FutureTask<>(work);
            executor.execute(this.task);
        }

        public boolean isDone() {
            return this.task.isDone();
        }

        public String getResponse() {
            try {
                return this.task.get();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class GetRequestWork implements Callable<String> {
        private final String url;

        public GetRequestWork(String url) {
            this.url = url;
        }
        public String getUrl() {
            return this.url;
        }
        public String call() throws Exception {
            return closeableHttpClient.execute(new HttpGet(getUrl()), new BasicResponseHandler());
        }
    }
}