
namespace pack {

class SimpleClass {


public:

  virtual int newOperation();

  double newAttr;
};

}


namespace pack {

int SimpleClass::newOperation()
{
  return 0;
}

}

