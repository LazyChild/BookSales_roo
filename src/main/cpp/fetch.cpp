//
//  File.cpp
//  BookSales_code
//
//  Created by Sarah Dong on 4/30/13.
//  Copyright (c) 2013 Sarah Dong. All rights reserved.
//

#include <iostream>
#include <fstream>
#include <stdlib.h>
using namespace std;

int main()
{
    ifstream file("/Users/sarah/Documents/Fudan/Database/workspace/BookSales_code/booklist.txt");
    while(!file.eof())
    {
        string temp;
        file>>temp;
        string url="http://api.douban.com/book/subject/isbn/"+temp;
        string command = "curl " + url + " > /Users/sarah/Documents/Fudan/Database/workspace/BookSales_code/booklistOutput/" + temp + ".txt";
        system(command.c_str());
    }
    
}


