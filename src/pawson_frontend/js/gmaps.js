var geocoder;
var map;
var x;
var y;
function initialize() {
  geocoder = new google.maps.Geocoder();
  var latlng = new google.maps.LatLng(-34.397, 150.644);
  var mapOptions = {
    zoom: 8,
    center: latlng
  }
  map = new google.maps.Map(document.getElementById('map'), mapOptions);
}

function codeAddress() {
  var address = document.getElementById('address').value;
  geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == 'OK') {
      map.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
          map: map,
          position: results[0].geometry.location
      });
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
    x = (results[0].geometry.viewport.Ia.hi + results[0].geometry.viewport.Ia.lo)/2;
    y = (results[0].geometry.viewport.Wa.hi + results[0].geometry.viewport.Wa.lo)/2;
    console.log(x);
    console.log(y);

  });
}

function buttonClick() {
  var request = new XMLHttpRequest();
  request.onreadystatechange = function(){
      if(this.readyState === 4 && this.status === 200){
        window.location.href = 'result.html';
        console.log(JSON.parse(this.responseText));
      }
  }
  var url = "http://localhost:8084/api/grading?coords=" + x + "," + y
  console.log(url);
  request.open("GET", url);
  request.send();
  console.log(request.value);
}