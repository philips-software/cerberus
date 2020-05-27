#include <cstdio>
using namespace std;

constexpr int maxstring = 1024;    // read buffer size
constexpr int repeat = 5;


#pragma warning( push )
#pragma warning( disable : 4100 )
int main( int argc, char ** argv ) {
    const char * fn = "testfile.txt";   // file name
    const char * str = "This is a literal c-string.\n";
    
    // create/write the file
    printf("writing file\n");
    FILE * fw = fopen(fn, "w");
    for(int i = 0; i < repeat; i++) {
        fputs(str, fw);
    }
    
    fclose(fw);
    printf("done.\n");
    
    // read the file
    printf("reading file\n");
    char buf[maxstring];
    FILE * fr = fopen(fn, "r");
    while(fgets(buf, maxstring, fr)) {
        fputs(buf, stdout);
    }
    
    fclose(fr);
    remove(fn);
    
    printf("done.\n");
    
    return 0;
}
#pragma warning( pop )