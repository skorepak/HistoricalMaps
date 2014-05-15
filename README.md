HistoricalMaps
==============

ToDO:

Design:
 - Search Box (action bar)
 - Info Box / Favorities box / Searched box
  - create element list design (Headline = "Favorities" / "Searched for" + syntax; for each element - name, nearCity)
  - create element design (Headline1 =  "Location Information" / "< Found Element", Headline2 = name / chosenLocation, textAbout, Headline2 = googleDB.placeName, position)
 - Navigation Drawer
  - Make headlines - Maps, About
  - In About add:
   - Authors
   - Application Info
   - Feedback
 - Icons
  - Menu icons
   - Home (Šumava)
   - Pin - ?? (Better add to Favorities - like + button)
   - Information
   - Favorities
  - Map icon
   - Pin icon (marker)

Functionality:
 - Main
  - make Historical map
   - cut maps appropriately
   - load maps in right place (GPS, 256x256 px)
   - create database table (places = id, position, radius, name, nearCity, textAbout)
  - create search engine (querry from our db and google maps db)
  - put marker (on put marker event)
  - add marked place to favorities
 - Marginal
