Deliveloo

Deliveloo est une application qui a pour objectif principal d’optimiser des tournées effectuées par des cyclistes qui vont chercher des colis pour les livrer. Cette application est inspirée d’un projet réel piloté par le Grand Lyon et visant à optimiser la mobilité durable en ville (voir www.optimodlyon.com). 
Lors du lancement de l’application un plan de la ville de Lyon est chargé puis l’utilisateur peut sélectionner une demande de livraisons à réaliser. Celle-ci est affichée relativement au plan. L’utilisateur peut ensuite calculer la tournée optimale pour cette demande. Les informations relatives aux horaires de livraisons sont alors indiquées et une feuille de route destinée au livreur avec le trajet à emprunter est générée. Pour finir l’utilisateur peut modifier la demande chargée en supprimant ou ajoutant des livraisons. La tournée est alors recalculée en essayant de minimiser l’impact sur les autres livraisons et leurs horaires. 


Technologies
L’application utilise le composant mapjfx et OpenLayers pour l’affichage de la carte, des marqueurs, labels et des lignes de trajets. Plus d’informations sur le projet mapjfx sont disponibles ici : site de sothawo.


Lancer l’application
Pour utiliser l’application il faut décompresser l’archive DelivelooCode.zip, ouvrir le projet avec un IDE et Run la classe Main du projet. 
Le code est dores et déjà compilé, si vous souhaitez le compiler de nouveau il faut veiller à disposer d’un IDE avec mvn.


Principaux cas d’utilisation

Charger et afficher un plan
Au lancement de l’application, un plan de la ville de Lyon est affichée. Les données cartographiques sont stockées dans une mémoire cache pour plus d’efficacité. La carte est interactive et utilise des données d’Open Street Map. 

Interactions :
- Se déplacer sur la carte par drag and drop avec la souris 
- Zoomer grâce à la molette de la souris, le slider de zoom ou bien en cliquant sur « Zoom initial » 
- Recadrer la carte comme initialement en cliquant sur « Recadrer la carte »
- Charger un autre plan en cliquant sur « Changer Plan » et en sélectionnant le fichier .xml correspondant au format d’un plan.

Charger et visualiser une demande
Vous pouvez charger un fichier XML décrivant des livraisons à effectuer. Il vous suffit pour cela de cliquer sur le bouton « Charger demande » puis de sélectionner votre fichier .xml de demande. L’application visualise alors la position de chaque adresse (de départ, et des points d’enlèvement et de livraison) sur le plan grâce à des marqueurs de couleur. Une couleur correspond à une livraison. Une flèche vers le bas indique un point de dépôt alors qu’une flèche vers le haut indique un point d’enlèvement. L’entrepôt est signalé par un drapeau.

Calculer et visualiser une tournée
Vous pouvez demander à calculer une tournée qui part de l’adresse de départ, visite les points d’enlèvement et de livraison (de sorte que, pour chaque demande, le point d’enlèvement soit visité avant le point de livraison), et revient à l’adresse de départ. La durée de la tournée est égale au temps nécessaire pour parcourir l’ensemble de ses tronçons, plus la durée de tous les enlèvements et livraisons.

Pour effectuer le calcul de la tournée optimale (de durée minimale) il vous faut cliquer sur le bouton « Calculer tournée ».
Un indicateur vous indique alors que le calcul est en cours et les tournées s’affichent progressivement. Vous pouvez interrompre ce calcul à tout moment en cliquant sur « STOP ». 

Modifier une demande et la tournée calculée correspondante
Vous pouvez modifier la tournée en supprimant des livraisons ou en ajoutant de nouvelles livraisons. Après chaque modification, le système met à jour la nouvelle tournée calculée qui minimise les changements de livraisons et leurs horaires par rapport à la tournée précédente.

Pour supprimer une livraison, sélectionnez la livraison à supprimer en cliquant sur un de ses 2 points (dépôt ou enlèvement) dans le panneau de détails des livraisons, puis cliquez sur « Supprimer ».

Pour ajouter une livraison, cliquez sur « Ajouter » puis faites un clic-droit sur la carte à l’endroit où vous souhaitez ajouter le point d’enlèvement, puis un cli-droit à l’endroit du point de dépôt et enfin entrez les durées de livraison et d’enlèvement correspondante dans la fenêtre qui s’est ouverte à cet effet.

Générer une feuille de route
Une fois la tournée calculée, vous pouvez générer une feuille de route du trajet pour le livreur. Un fichier .txt contenant les informations relatives au trajet à effectuer (nom de rue, distance à parcourir, à quelle intersection tourner et dans quelle direction) est alors exporté.
Pour générer la feuille de route il faut cliquer sur le bouton « Feuille de route » puis sélectionner le dossier où exporter le fichier.


Naviguer dans l’historique des tournées
Vous pouvez à tout moment naviguer dans l’historique des tournées calculées en cliquant sur les flèches “précédent” ou “suivant”. La tournée précédente ou suivante est ainsi affichée et remplace la précédent. 

