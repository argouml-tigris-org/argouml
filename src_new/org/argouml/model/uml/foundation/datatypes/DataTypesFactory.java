package org.argouml.model.uml.foundation.datatypes;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import java.util.List;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MArgListsExpression;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MIterationExpression;
import ru.novosoft.uml.foundation.data_types.MMappingExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MObjectSetExpression;
import ru.novosoft.uml.foundation.data_types.MProcedureExpression;
import ru.novosoft.uml.foundation.data_types.MTimeExpression;
import ru.novosoft.uml.foundation.data_types.MTypeExpression;

public class DataTypesFactory extends AbstractModelFactory {
    private static DataTypesFactory SINGLETON =
                   new DataTypesFactory();

    public static DataTypesFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private DataTypesFactory() {
    }

    public MActionExpression createActionExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MActionExpression expression = new MActionExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MArgListsExpression createArgListsExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MArgListsExpression expression = new MArgListsExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MBooleanExpression createBooleanExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MBooleanExpression expression = new MBooleanExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MExpression createExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MExpression expression = new MExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MIterationExpression createIterationExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MIterationExpression expression = new MIterationExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MMappingExpression createMappingExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MMappingExpression expression = new MMappingExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MObjectSetExpression createObjectSetExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MObjectSetExpression expression = new MObjectSetExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MProcedureExpression createProcedureExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MProcedureExpression expression = new MProcedureExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MTimeExpression createTimeExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MTimeExpression expression = new MTimeExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MTypeExpression createTypeExpression(String language, String body) {
	//  triple-spacing before paren hides from ant replace
        MTypeExpression expression = new MTypeExpression   (language, body);
	super.postprocess(expression);
	return expression;
    }

    public MMultiplicity createMultiplicity(int lower, int upper) {
	//  triple-spacing before paren hides from ant replace
        MMultiplicity multiplicity = new MMultiplicity   (lower, upper);
	super.postprocess(multiplicity);
	return multiplicity;
    }

    public MMultiplicity createMultiplicity(List range) {
	//  triple-spacing before paren hides from ant replace
        MMultiplicity multiplicity = new MMultiplicity   (range);
	super.postprocess(multiplicity);
	return multiplicity;
    }

    public MMultiplicity createMultiplicity(String str) {
	//  triple-spacing before paren hides from ant replace
        MMultiplicity multiplicity = new MMultiplicity   (str);
	super.postprocess(multiplicity);
	return multiplicity;
    }


}

