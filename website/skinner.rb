#!/usr/bin/ruby

require 'ftools'
require 'rexml/document'
require 'xemplate'
include REXML


BASE_DIR = "."
CONTENT_DIR = File.join(BASE_DIR,"content")
SKIN_DIR = File.join(BASE_DIR,"templates")
OUTPUT_DIR = File.join(BASE_DIR,"output")

IS_MARKUP = lambda { |file| file =~ /.(html|xml)$/ }

TEMPLATE = XEMPLATE::load_template( File.join(SKIN_DIR,"skin.html") )


def content_files
    Dir[File.join(CONTENT_DIR,"*")]
end

def content_html
    content_files.select &IS_MARKUP
end

def content_assets
    content_files.reject &IS_MARKUP
end

def skin_assets
    Dir[File.join(SKIN_DIR,"*")].reject &IS_MARKUP
end

def output_file( basename )
    File.join(OUTPUT_DIR,basename)
end

def copy_to_output( files )
    files.each do |asset_file|
        File.copy( asset_file, OUTPUT_DIR, :verbose )
    end
end

def load_xml( file )
    File.open(file) do |input|
        begin
            Document.new(input)
        rescue ParseException => ex
            raise ParseException,
                  file + ", line " + ex.line.to_s + ": " + ex.message, 
                  ex.backtrace
        end
    end
end

def skin_content_to_output
    content_html.each do |content_file|
        basename = File.basename(content_file)
        output_file = output_file(basename)
        config = {
            "content" => content_file,
            "isindex" => (content_file =~ /content\/index\.html$/) != nil
        }
        
        skinned_content = TEMPLATE.expand( config )
        
        # workaround for MSIE
        add_class( 
            skinned_content.elements["/html/body/div[@id='content']/*[1]"], 
            "FirstChild" )
        
        puts "#{content_file} ~> #{output_file}"
        write_to_output( skinned_content, output_file )
    end
end

def add_class( element, new_class )
    old_class = element.attributes["class"]
    if old_class != nil
        new_class = "#{old_class} #{new_class}"
    end
    element.add_attribute( "class", new_class )
end

def write_to_output( xhtml, output_file )
    File.open( output_file, "w" ) do |output|
        xhtml.write( output, 0, false, true )
    end
end

def delete_old_output
    Dir[File.join(OUTPUT_DIR,"*")].each do |file|
        File.delete(file)
    end
end

def build_site
    delete_old_output
    skin_content_to_output
    copy_to_output content_assets
    copy_to_output skin_assets
end

begin
    build_site
rescue REXML::ParseException => ex
    puts $stderr, "parse error: " + ex.message
    exit 1
end

