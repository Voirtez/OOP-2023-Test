package ie.tudublin;

import java.util.ArrayList;

import processing.core.PApplet;

public class DANI extends PApplet
{

	int mode = 0;

    ArrayList<Word> model;

    void printAll()
    {
        for(Word w:model)
        {
            println(w);
        }
    }


    Word findWord(String word)
    {
        for(Word w:model)
        {
            if (w.getWord().equalsIgnoreCase(word))
            {
                return w;
            }
        }
        return null;
    }

    public void loadFile()
    {
        model = new ArrayList<Word>();
        String[] lines = loadStrings("shakespere.txt");
        for(String line:lines)
        {
            String[] words = split(line, ' '); 
            for(int i = 0 ; i < words.length ; i ++)
            {
                String w = words[i];
                w = w.replaceAll("[^\\w\\s]","");
                Word newWord = findWord(w);
                if (newWord == null)
                {
                    newWord = new Word(w);
                    model.add(newWord);
                }
                if (i < words.length - 1)
                {
                    String nextWord = words[i + 1]; 
                    nextWord = nextWord.replaceAll("[^\\w\\s]","");
                    
                    Follow f = newWord.findFollow(nextWord);
                    if (f == null)
                    {
                        f = new Follow(nextWord);
                        newWord.follows.add(f);
                    }
                    f.setCount(f.getCount() + 1);
                }
            }
        }
    }

	public void settings() {
		size(1000, 1000);
		//fullScreen(SPAN);
	}

    String[] sonnet;

    public String[] writeSonnet()
    {
        String[] sonnet = new String[14];
        for(int i = 0 ; i < 14 ; i ++)
        {
            String line = "";
            int start = (int) random(0, model.size());
            Word w = model.get(start);
            for(int j = 0 ; j < 7 ; j ++)
            {            
                line += w.getWord() + " ";
                if (w.follows.size() > 0)
                {
                    int next = (int) random(0, w.follows.size());
                    Follow f = w.follows.get(next);
                    w = findWord(f.word);
                }
                else
                {
                    break;
                }
            }
            sonnet[i] = line;
            println(line);            
        }
        return sonnet;

    }

	public void setup()
    {
		colorMode(HSB);

        loadFile();
        printAll();
        println();
        sonnet = writeSonnet();
	}

	public void keyPressed()
    {
        sonnet = writeSonnet();
	}

	float off = 0;

	public void draw() 
    {
		background(0);
		fill(255);
		noStroke();
		textSize(20);
        textAlign(CENTER, CENTER);
        for(int i = 0 ; i < sonnet.length ; i ++)
        {
            float y = map(i, 0, 14, 100, height - 500);
            text(sonnet[i], width / 2, y);
        }
	}
}
