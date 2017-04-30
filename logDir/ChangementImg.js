function ChangementJoueur(element , dossier)
{
  var srcimg = element.id+".png";
  document.getElementById("grapheImg").src =  dossier+srcimg;
  document.getElementById("titlegraphe").innerHTML = "Graphe du Joueur "+element.id;
}


function ChangementProducteur(element , dossier)
{
  var srcimg = element.id+".png";
  document.getElementById("grapheImg").src = dossier+srcimg;
  document.getElementById("titlegraphe").innerHTML = "Graphe de la Ressource "+element.id;
}

