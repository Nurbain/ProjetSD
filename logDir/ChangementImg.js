function ChangementJoueur(element , dossier, nom)
{
  var srcimg = element.id+".png";
  document.getElementById("grapheImg").src =  dossier+srcimg;
  document.getElementById("titlegraphe").innerHTML = "Graphe du Joueur "+nom;
}


function ChangementProducteur(element , dossier, nom)
{
  var srcimg = element.id+".png";
  document.getElementById("grapheImg").src = dossier+srcimg;
  document.getElementById("titlegraphe").innerHTML = "Graphe de la Ressource "+nom;
}

function ChangementGL(element , dossier)
{
  var srcimg = element.id+".png";
  document.getElementById("grapheImg").src = dossier+srcimg;
  document.getElementById("titlegraphe").innerHTML = "Graphe de Tous les Joueurs";
}
