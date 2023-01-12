package kawa.pyszna.grading;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/grading")
@RequiredArgsConstructor
public class GradingController {
    private final GradingService gradingService;

    @GetMapping("/grading")
    public ResponseEntity<Integer> getGrading(@RequestParam List<Double> id) {
        
    }
}
