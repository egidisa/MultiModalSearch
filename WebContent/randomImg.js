function changeImg(min, max) { // create the function for changing the images
    var noi = max - min; // number of images
    var numRand = Math.floor(Math.random() * noi) + min; // randomized number
    $("#banner").find("img").attr('src','pages/gallery/PhotoWall/images/' + ""+ numRand +"" + '.jpg'); // set a new image
}

$(function() { // Waiting for the DOM ready
    setInterval(function(){ // create an interval (loop)
    changeImg(101, 120); // the function with paramteters
    },1000); // the interval in millisecondes --> 1000 = 1 second
});
