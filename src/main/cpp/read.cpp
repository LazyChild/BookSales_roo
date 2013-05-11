//
//  File.cpp
//  read
//
//  Created by Sarah Dong on 5/2/13.
//  Copyright (c) 2013 Sarah Dong. All rights reserved.
//

#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <string>
using namespace std;

int main()
{
    ifstream list("/Users/sarah/Documents/Fudan/Database/workspace/BookSales_code/booklist.txt");
    ofstream t("/Users/sarah/Documents/Fudan/Database/workspace/BookSales_code/bookInfo.txt");
    
    while(!list.eof())
    {
        string addr;
        getline(list, addr);
        
        ifstream file("/Users/sarah/Documents/Fudan/Database/workspace/BookSales_code/booklistOutput/"+addr+".txt");
        string rec[6];
        
        int check=0;
        
        while(!file.eof())
        {
            string a;
            getline(file, a);
            
            if(((signed long)a.find("<db:attribute name=\"isbn13\">")!=-1))
            {
                long t1=a.find(">")+1;
                long t2=a.find("</")-t1;
                rec[0]=a.substr(t1,t2);
            }
            
            if(((signed long)a.find("<db:attribute name=\"title\">")!=-1))
            {
                long t1=a.find(">")+1;
                long t2=a.find("</")-t1;
                rec[1]=a.substr(t1,t2);
            }
            
            if(((signed long)a.find("<db:attribute name=\"author\">")!=-1))
            {
                long t1=a.find(">")+1;
                long t2=a.find("</")-t1;
                if(rec[2] == "") rec[2]=a.substr(t1,t2);
            }
            
            if(((signed long)a.find("<db:attribute name=\"price\">")!=-1))
            {
                long t1=a.find(">")+1;
                long t2=a.find("</")-t1;
                rec[3]=a.substr(t1,t2);
            }
            
            if(((signed long)a.find("<db:attribute name=\"publisher\">")!=-1))
            {
                long t1=a.find(">")+1;
                long t2=a.find("</")-t1;
                rec[4]=a.substr(t1,t2);
            }
            
            if(((signed long)a.find("<link href=\"http://img3")!=-1))
            {
                long t1=a.find("\"")+1;
                long t2=a.find("\" ")-t1;
                rec[5]=a.substr(t1,t2);
            }
            
        }
        
        for(int i=0;i<6;i++)
        {
            if(rec[i]=="")
            {
                check=1;
            }
        }
        
        if(check==0)
        {
            for(int i=0;i<6;i++)
            {
                if(i==5) t<<"\""<<rec[i]<<"\""<<endl;
                else
                {
                    t<<"\""<<rec[i]<<"\""<<',';
                }
            }
        }
    }
    return 0;
}