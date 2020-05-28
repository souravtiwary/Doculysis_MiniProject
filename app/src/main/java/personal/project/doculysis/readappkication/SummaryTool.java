package personal.project.doculysis.readappkication;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collections;


public class SummaryTool {
    FileInputStream in;
    FileOutputStream out;
    ArrayList<Sentence> sentences, contentSummary;
    ArrayList<Paragraph> paragraphs;
    int noOfSentences, noOfParagraphs;

    double[][] intersectionMatrix;
    LinkedHashMap<Sentence,Double> dictionary;


    public SummaryTool(){
        in = null;
        out = null;
        noOfSentences = 0;
        noOfParagraphs = 0;
    }
    public void init(){

        sentences = new ArrayList<Sentence>();
        paragraphs = new ArrayList<Paragraph>();
        contentSummary = new ArrayList<Sentence>();
        dictionary = new LinkedHashMap<Sentence,Double>();
        noOfSentences = 0;
        noOfParagraphs = 0;
        //  extractSentenceFromContext();

    }

    public void extractSentenceFromContext(String path) {

        int nextChar, j = 0;
            int prevChar = -1;
            File file = new File(Environment.getExternalStorageDirectory(), path);
            String fileName = file.getName();
        //Toast.makeText(context,"Extracting Sentence form file", Toast.LENGTH_SHORT).show();

            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                while ((nextChar = in.read()) != -1) {
                    j = 0;
                    String temp = "";
                    while ((char) nextChar != '.') {
                        //System.out.println(nextChar + " ");
                        temp += (char) nextChar;
                        if ((nextChar = in.read()) == -1) {
                            break;
                        }
                        if ((char) nextChar == '\n' && (char) prevChar == '\n') {
                            noOfParagraphs++;
                        }
                        j++;
                        prevChar = nextChar;
                    }

                    sentences.add(new Sentence(noOfSentences, (new String(temp)).trim(), (new String(temp)).trim().length(), noOfParagraphs));
                    noOfSentences++;
                    prevChar = nextChar;
                }
                in.close();
                //    groupSentencesIntoParagraphs();
            } catch (Exception e) {
                e.printStackTrace();
            }

        //Toast.makeText(context,"Extracting Sentence form file is completed", Toast.LENGTH_SHORT).show();
    }


    public void groupSentencesIntoParagraphs(){
        int paraNum = 0;
        Paragraph paragraph = new Paragraph(0);

        //Toast.makeText(context,"Grouping Sentence into paragraph", Toast.LENGTH_SHORT).show();

        for(int i=0;i<noOfSentences;i++){
            if(sentences.get(i).paragraphNumber == paraNum){
                //continue
            }else{
                paragraphs.add(paragraph);
                paraNum++;
                paragraph = new Paragraph(paraNum);

            }
            paragraph.sentences.add(sentences.get(i));
        }

        paragraphs.add(paragraph);
        //createIntersectionMatrix();
    }
    public double noOfCommonWords(Sentence str1, Sentence str2){
        double commonCount = 0;

        for(String str1Word : str1.value.split("\\s+")){
            for(String str2Word : str2.value.split("\\s+")){
                if(str1Word.compareToIgnoreCase(str2Word) == 0){
                    commonCount++;
                }
            }
        }

        return commonCount;
    }

    public void createIntersectionMatrix(){
        intersectionMatrix = new double[noOfSentences][noOfSentences];
        //Toast.makeText(context,"Creating Matrix of sentence priority", Toast.LENGTH_SHORT).show();
        for(int i=0;i<noOfSentences;i++){
            for(int j=0;j<noOfSentences;j++){

                if(i<=j){
                    Sentence str1 = sentences.get(i);
                    Sentence str2 = sentences.get(j);
                    intersectionMatrix[i][j] = noOfCommonWords(str1,str2) / ((double)(str1.noOfWords + str2.noOfWords) /2);
                }else{
                    intersectionMatrix[i][j] = intersectionMatrix[j][i];
                }

            }
        }
        // createDictionary();
    }

    public void createDictionary(){

        //Toast.makeText(context,"Creating Dictionary...", Toast.LENGTH_SHORT).show();

        for(int i=0;i<noOfSentences;i++){
            double score = 0;
            for(int j=0;j<noOfSentences;j++){
                score+=intersectionMatrix[i][j];
            }
            dictionary.put(sentences.get(i), score);
            ((Sentence)sentences.get(i)).score = score;
        }
        //createSummary();
    }

    public void createSummary(){

        //Toast.makeText(context,"Summary is ready :)", Toast.LENGTH_SHORT).show();

        for(int j=0;j<=noOfParagraphs;j++){
            int primary_set = paragraphs.get(j).sentences.size()/5;

            //Sort based on score (importance)
            Collections.sort(paragraphs.get(j).sentences,new SentenceComparator());
            for(int i=0;i<=primary_set;i++){
                contentSummary.add(paragraphs.get(j).sentences.get(i));
            }
        }

        //To ensure proper ordering
        Collections.sort(contentSummary,new SentenceComparatorForSummary());
        printSummary();
    }
    public String printSummary(){

//		System.out.println("no of paragraphs = "+ noOfParagraphs);
        String ans="";
        for(Sentence sentence : contentSummary){
            //tv_output.setText(sentence.value);
            ans+=sentence.value + ".";
        }
        return ans;
    }

}