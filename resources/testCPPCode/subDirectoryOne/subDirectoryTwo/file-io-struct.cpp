#include <cstdio>
#include <cstring>
#include <cstdint>
using namespace std;

constexpr size_t maxlen = 111;

struct S {
    uint8_t num;
    uint8_t len;
    char str[maxlen + 1];
};

int main( int argc, char ** argv ) {
    const char * fn = "test.file";            // file name
    const char * cstr = "This is a literal C-string.";
    
    // create/write the file
    printf("writing file\n");
    FILE * fw = fopen(fn, "wb");
    
    static struct S buf1;
    for( int i = 0; i < 5; i++ ) {
        buf1.num = i;
        buf1.len = (uint8_t) strlen(cstr);
        if(buf1.len > maxlen) buf1.len = maxlen;
        strncpy(buf1.str, cstr, maxlen);
        buf1.str[buf1.len] = 0; // make sure it's terminated
        fwrite(&buf1, sizeof(struct S), 1, fw);
    }
    
    fclose(fw);
    printf("done.\n");
    
    // read the file
    printf("reading file\n");
    FILE * fr = fopen(fn, "rb");
    struct S buf2;
    size_t rc;
    while(( rc = fread(&buf2, sizeof(struct S), 1, fr) )) {
        printf("a: %d, b: %d, s: %s\n", buf2.num, buf2.len, buf2.str);
    }
    
    fclose(fr);
    remove(fn);
    
    printf("done.\n");
    
    return 0;
}
