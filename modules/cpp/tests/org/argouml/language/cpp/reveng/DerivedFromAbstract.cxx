// This example shows that the reveng module supports revenging a class which 
// is derived from an abstract class. It also shows correct protected, private 
// and public access specifiers are correctly reversed.
// Use of modifiers (e.g., unsigned) is included, as of pointers, references 
// and of user types as parameters and return values.

struct Dummy {
    signed char c;
};

class Base {
public:
    virtual unsigned int foo(Base& other) = 0;
protected:
    unsigned long ui;
    // and a protected method, to which the derived class still has access
    Dummy makeMeADummy() { 
       Dummy d;
       d.c = '4';
       helperMethod(&d.c);
       return d;
    }
private:
    void helperMethod(signed char* cstr);
};

class Derived: public Base {
    Dummy* pDum;
public:
    Derived(): Base(), pDum(0) {
       ui = 0;
    }
    ~Derived() {
        if (pDum) delete pDum;
    }
    unsigned int foo(Base& other) {
       if (!pDum) {
           pDum = new Dummy;
           *pDum = makeMeADummy();
       }
       // the next would cause problems if this were ever to run
       return 3 * 2 + pDum->c + other.foo(*this);
    }
};

void Base::helperMethod(signed char* cstr) {
    // dumb thing, simply does nothing
}
