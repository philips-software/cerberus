#include <cstdio>
#include <string>
#include <typeinfo>
using namespace std;

string func() {
    return string("this is a string");
}

#if defined (_MSC_VER)
  #if (_MSC_VER > 1200)
    #pragma warning( disable : 4996)
  #endif
#endif

int main() {
    auto x = func();
    printf("x is %s\n", x.c_str());
    if(typeid(x) == typeid(string)) puts("x is string");
    return 0;
}
