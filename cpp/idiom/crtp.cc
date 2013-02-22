// http://en.wikibooks.org/wiki/More_C%2B%2B_Idioms/Curiously_Recurring_Template_Pattern
// CRTP idiom: Curiously Recurring Template Pattern
// 
#include <stdio.h>

template <class Derived>
struct base {
	void interface() {
		static_cast<Derived*>(this)->implementation();
	}

	static void static_interface() {
		Derived::static_implementation();
	}

	// The default implementation may be (if exists) or should be (otherwise)
	// overriden by inheriting in derived classes (see below)
	void implementation();
	static void static_implementation();
};

struct derived_1 : base<derived_1> {
	static void static_implementation() {
		printf("derived_1 static_implementation()\n");
	}
};

struct derived_2 : base<derived_2> {
	static void implementation() {
		printf("derived_2 implementation()\n");
	}
};

int main(int argc, char **argv) {
	derived_1::static_interface();
	derived_2 d2;
	d2.interface();
	return 0;
}
