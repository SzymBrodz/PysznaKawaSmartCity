import {
    getX,
    getY
} from '../js/gmaps.js';

var x;
var y;

function buttonClick() {
    x = getX();
    y = getY();
    console.log(x);
    console.log(y);
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(this.readyState === 4 && this.status === 200){

        }
    }
    request.open("GET", "http://localhost:8084/api/grading?")
    //window.location.href = 'result.html';
}
