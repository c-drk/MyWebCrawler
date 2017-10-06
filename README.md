# MyWebCrawler


This is a solution for the following exercise:


You'll have to create a web crawler using scraping techniques to extract the first 30 entries 
from https://news.ycombinator.com/ You'll only care about the title, number of the order, the amount of comments 
and points for each entry.

![scrapper-challenge](https://user-images.githubusercontent.com/29734970/31269183-faa60c34-aa44-11e7-9f2e-aaea13295d6e.png)

From there, we want it to be able to perform a couple of filtering operations:

Filter all previous entries with more than five words in the title ordered by amount of comments first.
Filter all previous entries with less than or equal to five words in the title ordered by points.

Solution:

The web crawler extracts the main structure from the website which is in a html table. Then, the software analyze the 
information and returns a result.

The final form looks like this:

![webcrawler](https://user-images.githubusercontent.com/29734970/31269316-88d3a4e4-aa45-11e7-8f53-7684fdeb6cc0.png)






