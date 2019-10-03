import java.io.*;

public class CapitalOne {

    int codeLines = 0, commentLines = 0, totalLines = 0, singleComments=0, blockComments=0, blockCommentLines=0, countTODOs=0;
    boolean commentStart = false;
    static File fileName = null;
    String[] words = null;
    String wTodo = "TODO";

    public static void main(String[] arg) {
        if (0 < arg.length) {
            //  file will be passed during run time
            fileName = new File(arg[0]);
        } else {
            System.out.println("File not found: " + fileName);
        }
        CapitalOne obj = new CapitalOne();
        obj.fileAnalysis();
    }

    public void fileAnalysis() {
        BufferedReader br = null;
        String sRecentLine = null;
        boolean sameLine = false;

        try {
            br = new BufferedReader(new FileReader(fileName));

            while ((sRecentLine = br.readLine()) != null) 
            {
                sRecentLine = sRecentLine.trim();
                sameLine = false;
                while(sRecentLine != null && sRecentLine.length() > 0) 
                {
                    sRecentLine = lineAnalysis(sRecentLine, sameLine);
                    sameLine = true;
                }
            }

            totalLines = codeLines + commentLines;
            blockCommentLines = commentLines - singleComments;
            System.out.println("Total # of lines : " + totalLines);
            System.out.println("Total # of comment lines: " + commentLines);
            System.out.println("Total # of single line comments: " + singleComments);
            System.out.println("Total # of comment lines within block comments: " + blockCommentLines);
            System.out.println("Total # of block line comments: " + blockComments);
            System.out.println("Total # of TODO's: " + countTODOs);
            
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                // close bufferReader
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public String lineAnalysis(String sRecentLine, boolean sameLine) {        
        
        if(commentStart && sRecentLine.contains("*/")) {
            if (!sameLine)
                commentLines++;
            commentStart = false;
            if (!sRecentLine.endsWith("*/"))
                sRecentLine = sRecentLine.substring(sRecentLine.indexOf("*/")+2).trim();
            else
                sRecentLine = null;
        }
        else if(sRecentLine.startsWith("//")) {
            if (!sameLine)
                commentLines++;
                singleComments++;
            sRecentLine = null;
        }
        else if(sRecentLine.contains("/*")) {            
            commentStart = true;
            blockComments++;
            if (!sRecentLine.startsWith("/*")){
                if (!sameLine)
                    codeLines++;
                sRecentLine = sRecentLine.substring(sRecentLine.indexOf("/*")).trim();                
            }
            else {
                if (!sameLine)
                    commentLines++;
                if (sRecentLine.contains("*/")) {
                    commentStart = false;
                    if (!sRecentLine.endsWith("*/"))
                        sRecentLine = sRecentLine.substring(sRecentLine.indexOf("*/")+2).trim();
                    else
                        sRecentLine = null;
                }
                else
                    sRecentLine = null;
            }
        }        
        else if(commentStart) {
            if (!sameLine)
                commentLines++;
            sRecentLine = null;
        }
        else {
            commentStart = false;
            if (!sameLine)
                codeLines++;
            if (sRecentLine.contains(";")) {
                if (!sRecentLine.endsWith(";")) {
                    sRecentLine = sRecentLine.substring(sRecentLine.indexOf(";")+1).trim();
                }
                else
                    sRecentLine = null;
            }
            else
                sRecentLine = null;
        }
        words=sRecentLine.split(" ");  
          for (String word : words) 
          {
                 if (word.equals(wTodo))  
                 {
                   countTODOs++;
                 }
          }
        return sRecentLine;
    }
}