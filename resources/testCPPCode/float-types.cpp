#include <cstdio>
using namespace std;

// a byte is 8 bits
constexpr size_t byte = 8;

int main() {
    float f;
    double df;
    long double ldf;
    
    printf("size of float f is %ld bits\n", sizeof(f) * byte);
    printf("size of double float df is %ld bits\n", sizeof(df) * byte);
    printf("size of long double float ldf is %ld bits\n", sizeof(ldf) * byte);
    
    return 0;
}
