#!/usr/bin/env ruby

require 'ftools'
require 'rexml/document'
require 'xemplate'
require 'uri'
include REXML

BASE_DIR = "."
CONTENT_DIR = File.join(BASE_DIR,"content")
SKIN_DIR = File.join(BASE_DIR,"templates")
OUTPUT_DIR = File.join(BASE_DIR,"output")

CVSWEB_ROOT = "http://cvs.jmock.codehaus.org/jmock/website/content/"
DIST_ROOT = "http://dist.codehaus.org/jmock"
JAVADOC_ROOT = "/docs/javadoc"

SNAPSHOT_VERSION = ENV["BUILD_TIMESTAMP"]
PRERELEASE_VERSION = ENV["PRERELEASE_VERSION"]
RELEASE_VERSION = ENV["RELEASE_VERSION"]

NO_VERSION = "n/a"

TEMPLATE = XEMPLATE::load_template( File.join(SKIN_DIR,"skin.html") )

CONTENT_PATH = "/html/body/div[@id='center']/div[@id='content']"

BASE_URL = URI.parse("http://www.jmock.org/")


$logger = $stdout
def log( message )
    $logger.puts( message )
    $logger.flush
end

def jmock_jar( version )
    "#{DIST_ROOT}/jars/jmock-#{version}.jar"
end

def jmock_cglib_jar( version )
    "#{DIST_ROOT}/jars/jmock-cglib-#{version}.jar"
end

def jmock_src_jar( version )
    "#{DIST_ROOT}/distributions/jmock-#{version}-src.jar"
end

def jmock_javadoc_jar( version )
    "#{DIST_ROOT}/distributions/jmock-#{version}-javadoc.jar"
end


def is_markup( filename )
    filename =~ /.(html)$/
end

def is_cvs_data( filename )
    filename =~ /\/CVS(\/|$)/
end

def is_directory( filename )
    FileTest.directory? filename
end

def list_files dir
    Dir[File.join(dir,"*")].reject {|f| is_cvs_data(f)}
end

def skin_assets
    Dir[File.join(SKIN_DIR,"*")].reject {|f| is_cvs_data(f) or is_markup(f)}
end

def output_file( asset_file, root_asset_dir )
    File.join( OUTPUT_DIR, filename_relative_to(asset_file,root_asset_dir) )
end

def filename_relative_to( file, root_dir )
    if file[0,root_dir.length] != root_dir
        raise "#{file} is not within directory #{root_dir}"
    end
    
    root_dir_length = root_dir.length
    root_dir_length = root_dir_length + 1 if root_dir[-1] != '/'
    
    file[root_dir_length, file.length-root_dir_length]
end

def copy_to_output( asset_file, root_asset_dir )
    dest_file = output_file( asset_file, root_asset_dir )
    
    log "#{asset_file} -> #{dest_file}"
    
    File.copy( asset_file, dest_file )
end

def make_output_directory( asset_dir, root_asset_dir )
    new_dir = output_file( asset_dir, root_asset_dir )
    
    if not File.exists? new_dir
        log "making directory #{new_dir}"
        
        Dir.mkdir( new_dir )
    end
end

def skin_content( content_dir, root_content_dir=content_dir )
    list_files(content_dir).each do |content_file|
        if is_directory(content_file)
            make_output_directory( content_file, root_content_dir )
            skin_content( content_file, root_content_dir )
        elsif is_markup(content_file)
            skin_content_file( content_file, root_content_dir )
        else
                copy_to_output( content_file, root_content_dir )
            end
    end
end

def skin_content_file( content_file, root_content_dir )
    output_file = output_file( content_file, root_content_dir )

    log "#{content_file} ~> #{output_file}"
    
    config = {
        "content" => content_file,
        "isindex" => (content_file =~ /content\/index\.html$/) != nil,
        "history" => CVSWEB_ROOT + content_file[(root_content_dir.size+1)..-1]
    }

    [["snapshot",   SNAPSHOT_VERSION,   "SNAPSHOT"],
     ["prerelease", PRERELEASE_VERSION, PRERELEASE_VERSION],
     ["release",    RELEASE_VERSION,    RELEASE_VERSION]].each do |id,version,fileid|
        config["#{id}_available"] = version != nil
        config[id] = String.new(version || NO_VERSION)
        if version != nil
            config["#{id}_jmock_jar"] = jmock_jar(fileid)
            config["#{id}_jmock_cglib_jar"] = jmock_cglib_jar(fileid)
            config["#{id}_jmock_src_jar"] = jmock_src_jar(fileid)
            config["#{id}_jmock_javadoc_jar"] = jmock_javadoc_jar(fileid)
        end
    end

    skinned_content = TEMPLATE.expand( config )

    add_class( skinned_content.elements["#{CONTENT_PATH}/*[1]"], "FirstChild" )
    convert_javadoc_links( skinned_content )
    add_print_footnotes( skinned_content )

    write_to_output( skinned_content, output_file )
end

def add_class( element, new_class )
    old_class = element.attributes["class"]
    if old_class != nil
        new_class = "#{old_class} #{new_class}"
    end
    element.add_attribute( "class", new_class )
end

def convert_javadoc_links( skinned_content )
    skinned_content.each_element "#{CONTENT_PATH}//a" do |link|
            href = link.attributes["href"]
            
            if href =~ /^java:(.*)/
                link.attributes["href"] = class_to_javadoc_url($1)
            end
    end
end

def class_to_javadoc_url( name )
    path = name.split(".")
    if path.last == "*"
        path[path.size-1] = "package-summary"
    end
    
    "#{JAVADOC_ROOT}/#{path.join("/")}.html"
end

def add_print_footnotes( skinned_content )
    footnotes_div = Element.new("div")
    footnotes_div.add_attribute( "class", "LinkFootnotes" )
    footnotes_div.add_element( "p", {"class", "LinkFootnotesHeader"} ).add_text("Links:");

    footnote_index = 1

    skinned_content.each_element "#{CONTENT_PATH}//a[@href]" do |link|
        href = BASE_URL.merge( link.attributes["href"] )

        footnote = footnotes_div.add_element("p")
        footnote.add_text( footnote_index.to_s + ". " );
        footnote.add_element( "a", {"href" => href.to_s } ).add_text( href.to_s )

        footnote_ref = Element.new("span")
        footnote_ref.add_attribute( "class", "LinkFootnoteRef" )
        footnote_ref.add_element("sup").add_text(footnote_index.to_s)
        link.parent.insert_after( link, footnote_ref )
        
        footnote_index = footnote_index + 1
    end

    if footnotes_div.size > 1
        skinned_content.elements[CONTENT_PATH].add( footnotes_div )
    end
end

def write_to_output( xhtml, output_file )
    File.open( output_file, "w" ) do |output|
        xhtml.write( output, -1, true, true )
    end
end

def build_site
    skin_content CONTENT_DIR
    skin_assets.each {|f| copy_to_output f, SKIN_DIR}
    copy_to_output( "../CHANGELOG", ".." )
end

begin
    build_site
    $stdout.puts "done"
    $stdout.flush
rescue REXML::ParseException => ex
    $stderr.puts "parse error: " + ex.message
    $stderr.flush
    
    exit 1
end

