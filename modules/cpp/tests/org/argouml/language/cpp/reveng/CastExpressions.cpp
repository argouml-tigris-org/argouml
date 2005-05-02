
inline int f2(int i) {
  return i;
}

template<class T> class Clazz {
protected:
  virtual char f(T t, char) const {
    return ((char)f2((T)(t)));
  }
};
