
template<class _E> class II {
};

template<class _E, class _II = II<_E> >
class UnionInlineDefInMember {
private:
  virtual _II methodWithUnionInlineDef(long x) const 
  {union UnionName {
      void *Pv;
      unsigned long Lo[1 + sizeof((void *) - 1) / sizeof(unsigned long)];
    } _U;
    const int _NL = sizeof(_U.Lo) / sizeof(unsigned long);
    return II<long>();
  }
};

