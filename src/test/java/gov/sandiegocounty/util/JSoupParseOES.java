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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
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
public class JSoupParseOES {

//	final static String OES_DATA_SRC = "C:\\xampp\\htdocs\\www.sdcountyemergency.com";
	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com";
	static int elem_cnt = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
/*
 * TO DO
 * process elements, remove from top of stack to avoid nested, duplicated elems 
 * 
 */

		try {

			Path path = Paths.get(OES_DATA_SRC);
			final List<Path> files = new ArrayList<>();

			Files.walkFileTree(path,

					new SimpleFileVisitor<Path>() {

						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
							throws IOException {

							if (!attrs.isDirectory() && String.valueOf(file).endsWith(".html")) {

								files.add(file);
							}
							return FileVisitResult.CONTINUE;

						}
					});
//files.forEach(x-> {
//// LIST AVAIL FILES
//	System.out.printf( " \nJSoupParseOES :: main %d %s", elem_cnt++, x.toString() 
//	);
//}
//);

/*
 * test
 */
//			int page_idx = 284; // home/index
			int page_idx = 3; // donations/index
//			int page_idx = 138; // about/index
			ParseFile p = new ParseFile();

System.out.println( " JSoupParseOES :: main FILE UT " + files.get(page_idx).toString());
Element e = p.doParseFile(files.get(page_idx));//284

// TEST various pages explicitly 
////System.out.println( " JSoupParseOES :: main " + files.get(1).toString());
////e = p.doParseFile(files.get(1));//284

			p = null;

//////////////////

//////////////////
/*
 * TEST H*
 * 0 Call JSoupParseOES(file)] 
 * 1 -> invoke doParseFile
 * 2 --> parse content @ page center
 * 3 ---> invoke PageObj(title element)
 * 4 -----> invoke HttpURLConnectionRunner(title element, file/node path, internal_props_path )
 */

		PageObj page = new PageObj();
e.attributes().asList().stream().map(Attribute::getValue).collect(Collectors.toList());

			page.processPage(
				files.get(page_idx).toFile(), e.getAllElements()
			);


		page = null;
		p = null;
		e = null;

/*
 * 3
 */
// PageObj work ...

/*
 * 4
 */
// PageObj -> HttpURLConnectionRunner work ...

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
//				System.out.println(file.getFileName());
			}
			return FileVisitResult.CONTINUE;
		}
	}

	static class ParseFile {

		public Element doParseFile( Path file ) throws IOException {
			
			Document doc;
/*
 * 
 */
			
			doc = Jsoup.parse( new File(file.toString() ) , "UTF-8");

			Element misc	 		= doc.selectFirst( "div.page-header > h1");
			Element content 		= doc.selectFirst( "div.container > div.row > div.col-sm-9");
//			Elements content_row	= content.select("div.xrm-editable-html.xrm-attribute > div.xrm-attribute-value");//,div.row,div.oes-box


			final Map<String, String> manifest = new TreeMap<>();
			Elements test = new Elements();
			try {

				test = content.getElementsByClass("no-value");

			} catch (NullPointerException e) {

			}
			if( null != test && test.size() > 0 ) {
				content.select("div.xrm-attribute.no-value").remove();
			}
System.err.println( "clean results");
if( misc != null && !misc.getAllElements().isEmpty() ) {
	content = content.insertChildren(0, misc);
}

/*
System.out.println("** o 0 content " + content.children().size() );
content = content.insertChildren(0, misc);
System.out.println("** o 1 content " + content.children().size() );
System.out.println("** o 2 content " + content );
*/

/*
 * TEST H*
 * 0 Call JSoupParseOES(file)] 
 * 1 -> invoke doParseFile
 * 2 --> parse content @ page center
 * 3 ---> invoke PageObj(title element)
 * 4 -----> invoke HttpURLConnectionRunner(title element, file/node path, internal_props_path )
 */

/*
 * 4
 */
	// PageObj -> HttpURLConnectionRunner work ...
	
/////////////
System.err.println(file.toString());

		return content;
		}

	}

//	static class ParseRightRailFromFile {
//
//		public Element doParseFile( Path file ) throws IOException {
//			
//			Document doc;
///*
// * 
// */
//			doc = Jsoup.parse( new File(file.toString() ) , "UTF-8");
//
//			Element right_rail 		= doc.selectFirst( "div.col-sm-3");
//			Elements right_railcontent	= right_rail.select( "div.row");
//			String commented_right_rail = (">> TBD: common rail components - add to template <<")
//				.concat( new Comment( right_railcontent.html() ).toString() );
//
//
//			final Map<String, String> manifest = new TreeMap<>();
//			Elements test = new Elements();
//			try {
//
//				test = right_rail.getElementsByClass("no-value");
//
//			} catch (NullPointerException e) {
//
//			}
//
//			if( null != test && test.size() > 0 ) {
//				right_rail.select("div.xrm-attribute.no-value").remove();
//			}
//
//		return right_rail;
//		}
//
//	}

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