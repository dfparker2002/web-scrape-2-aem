/**
 * 
 */
package gov.sandiegocounty.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import gov.sandiegocounty.oes.model.PageObj;

/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 *	Refs
 *		https://stackoverflow.com/questions/2056221/recursively-list-files-in-java#24006711
 *		https://stackoverflow.com/questions/20987214/recursively-list-all-files-within-a-directory-using-nio-file-directorystream
 *		https://kodejava.org/how-to-recursively-list-all-text-files-in-a-directory/
 *		https://www.javabrahman.com/quick-tips/java-recursively-list-files-subdirectories-directory-examples/
 */
public class JSoupAnalyzeOES_PageCSS_Structure {

	final static String OES_DATA_SRC = "C:\\xampp\\htdocs\\www.sdcountyemergency.com";
	/**
	 * @param args
	 */
	public static void main(String[] args) {


		try {

			Path path = Paths.get(OES_DATA_SRC);
			final List<Path> files = new ArrayList<>();
			Elements test = new Elements();
			final Map<String, Elements> manifest = new TreeMap<>();
			final Map<String, String> analysis_coll = new TreeMap<>();
			
//analysis
			Files.walkFileTree(path,

// new FindTextFilesVisitor() {
					new SimpleFileVisitor<Path>() {

						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

							if (!attrs.isDirectory() && String.valueOf(file).endsWith(".html")) {

								files.add(file);

//final Elements e = new ParseFile().doParseFile(file);
manifest.put( (file.toFile().getPath()), new ParseFile().doParseFile(file) );

							}
							return FileVisitResult.CONTINUE;

						}
					});

/*
 * 
 */
///////////////////

			if( !manifest.isEmpty() ) {
//System.err.println( " JSoupAnalyzeOES_PageCSS_Structure :: manifest NOT empty" );
				manifest.forEach( (file_path,y)-> {
System.out.println( " JSoupAnalyzeOES_PageCSS_Structure :: main " + file_path);
System.out.println( " JSoupAnalyzeOES_PageCSS_Structure :: main " + y);
if( null != y ) {
y.forEach( elem -> {
//					final String fn = String.format( "%s", x );
					if(analysis_coll.containsKey(file_path)) {
	

						analysis_coll.put(file_path, analysis_coll.get(file_path)
							.concat(" > ")
							.concat(elem.tagName()
								.concat( 
									elem.attributes().hasKey("id") 
									? "#".concat( elem.attributes().get("id") )
									: ""
								)
								.concat(
									elem.attributes().hasKey("class") 
									? ".".concat( elem.attributes().get("class").replaceAll(" ", "."))
									: ""
								)
						));
					} else {
						analysis_coll.put(file_path, elem.tagName());
					}
}); // end iterate elem
} // end elem null check
				});

			} // end if-null
			
			final String analysis = analysis_coll.toString().replaceAll(",", "\n");
System.out.println( " JSoupParseOES.ParseFile :: doParseFile : collect tag paths " + analysis );

			Files.write(Paths.get("src/test/resources/pages_analysis.txt"), analysis.getBytes());

///////////////////

		} catch (IOException e) {

			e.printStackTrace();
		}

	}


	/**
	 * FindTextFilesVisitor.
	 * src: https://kodejava.org/how-to-recursively-list-all-text-files-in-a-directory/
	 */
	static class FindTextFilesVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

			if (file.toString().endsWith(".html")) {
				System.out.println(file.toUri() );

			}
			return FileVisitResult.CONTINUE;
		}
	}

	static class ParseFile {

		public Elements doParseFile( Path file ) throws IOException {
			
			Document doc;
/*
 * 
 */
			doc = Jsoup.parse( new File(file.toString() ) , "UTF-8");
			Element content 		= doc.selectFirst( "div.container > div.row > div.col-sm-9");

//			Elements right_rail 		= doc.select( "div.col-sm-3");


final Map<String, String> manifest = new TreeMap<>();
Elements test = new Elements();
try {
	test = content.getElementsByClass("no-value");
} catch (NullPointerException e) {

//	e.printStackTrace();
}
if( null != test && test.size() > 0 ) {
	content.select("div.xrm-attribute.no-value").remove();
}
if( null != content && content.getAllElements().size() > 0 ) {
//
//content.getAllElements().forEach(x-> {
//	final String fn = String.format( "%s", file.toFile().getPath() );
//	if(manifest.containsKey(fn)) {
//
//		
//		manifest.put(fn, manifest.get(fn)
//			.concat(" > ")
//			.concat(x.tagName()
//				.concat( 
//					x.attributes().hasKey("id") 
//					? "#".concat( x.attributes().get("id") )
//					: ""
//				) 
//				.concat(
//					x.attributes().hasKey("class") 
//					? ".".concat( x.attributes().get("class").replaceAll(" ", "."))
//					: ""
//				)
//		));
//	} else {
//		manifest.put(fn, x.tagName());
//	}
//});

//final String analysis = manifest.toString().replaceAll(",", "\n");
//System.out.println( " JSoupParseOES.ParseFile :: doParseFile : collect tag paths " + analysis );
//
//Files.write(Paths.get("src/test/resources/pages_analysis.txt"), analysis.getBytes());
					return content.getAllElements();
			} else return null;
		}

	}

	/**
	 * @param node
	 */
	private static void walkTree(Node node, String tgt) {
		for (int i = 0; i < node.childNodeSize();) {

			Node child = node.childNode(i);
			if (child.nodeName().equals(tgt)) {
//				child.remove(); // do something

			} else {

				walkTree(child, tgt);
				i++;
			}
		}
	}

}