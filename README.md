# Client-Server-Autocomplete-Search-Spring-2020
Class work for Distributed Systems

This is a combination of two seperate projects. The first involved implementing an autocomplete search on a list of weighted terms and displaying the top results. You run `Autocomplete` and pass it a list of weighted terms (samples provided in `data/`) and the number of results to show, to test this functionallity. You can also test it using `AutocompleteGUI`. 

The second phase of this project involved implementing a simple client server seystem, where the client will send the server a search query and the server will perform the search, sending the results back to the client. `Server` must be passed a port number, and `Client` must be passed a port, search query, terms file, as well as an optional argument for the number of terms to search. 

Note: The `main` methods of `Terms.java`, `BinarySearchDeluxe.java`, and `Autocomplete.java` (these are essentially just test methods), as well as the entierty of `AutocompleteGUI.java` and `extra.jar` were proveded to me by my professor and not writen by me. Everything related to the actual implementation however, is all my code. Details about my implementation can be found in `report.txt` (for the autocomplete section), and `report5.txt` (for the client-server section).

The master branch has been renamed to prevent this repo from [showing up in search the results](https://webapps.stackexchange.com/questions/67344/how-to-prevent-a-public-github-repository-from-showing-up-on-search-results) of people potentially taking this course.
