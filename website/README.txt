This is a prototype for the jMock website.  It's not complete: a lot of pages have
not yet been written or linked in.

Here's what I've tried to achieve:

1) It implements the design that resulted from the analysis of stereotypical users

The menu is designed to put what our users most want right up front.  Other, less
important information can be linked from subpages.

2) It completely separates content, navigation and style

This makes it easy to change the "skin" of the site or the style of that skin.

Skinning is implemented by a simple XML templater that I knocked up in Ruby.  
I "spiked" it very quickly:  it works but there are no tests apart from the site 
looks ok.  Perhaps it should be reimplemented in Java with tests etc. and used
as a jMock example.

3) The navigation "skin" is designed to be convenient in browsers that do not do CSS

The banner appears first, then the main page content, followed by any side panels on 
the page.  Thus, users of less capable browsers get the most important information
first and less important information later.

4) All content is simple XHTML.

No styles required, although any styles and other media used by the content pages 
get merged into the final site by the templater.  This should make it easy to create
content in WYSIWYG editors.


The directories should be self explanatory:
  ./skin:        contains the skin XML/XHTML and style sheet
  ./content:     contains the content XHTML
  ./output:      is where the templater builds the skinned pages
  ./skinner.rb:  is the program that builds the site
  ./xemplate.rb: is the templating engine used by the skinner

To run the skinner you will need Ruby 1.8.  It might work with older versions of Ruby
if you have installed REXML.


Things that need to be done:

1) A decent logo
2) Write the rest of the pages
3) Integrate any project reports into the skin
