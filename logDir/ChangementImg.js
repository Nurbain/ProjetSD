function ChangementJoueur(element , nomFichier)
{
  var srcimg = nomFichier+"J"+element.id+".png";
  document.getElementById("grapheImg").src =  srcimg;
}


function ChangementProducteur(element)
{
  var srcimg = nomFichier+"P"+".png";
  document.getElementById("grapheImg").src =  srcimg;
}
