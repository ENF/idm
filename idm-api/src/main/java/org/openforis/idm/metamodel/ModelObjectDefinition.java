package org.openforis.idm.metamodel;

import java.util.List;

import org.openforis.idm.metamodel.ModelObjectLabel.LabelType;

/**
 * @author G. Miceli
 * @author M. Togna
 */
public interface ModelObjectDefinition extends Versionable, Annotatable, ModelObjectDefinitionContainer {

	/**
	 * @return Returns the parent.
	 * @uml.property name="parentDefinition"
	 * @uml.associationEnd inverse="children:org.openforis.idm.metamodel.EntityDefinition"
	 */
	EntityDefinition getParentDefinition();

	/**
	 * @return Returns the name.
	 * @uml.property name="name"
	 */
	String getName();

	/**
	 * @return Returns the requiredExpression.
	 * @uml.property name="requiredExpression"
	 */
	String getRequiredExpression();

	/**
	 * @return Returns the minCount.
	 * @uml.property name="minCount"
	 */
	Integer getMinCount();

	/**
	 * @return Returns the maxCount.
	 * @uml.property name="maxCount"
	 */
	Integer getMaxCount();

	/**
	 * @return Returns the relevantExpression.
	 * @uml.property name="relevantExpression"
	 */
	String getRelevantExpression();

	/**
	 * @return Returns the descriptions.
	 * @uml.property name="descriptions"
	 */
	List<LanguageSpecificText> getDescriptions();

	/**
	 * @return Returns the labels.
	 * @uml.property name="labels"
	 */
	List<ModelObjectLabel> getLabels();

	List<ModelObjectLabel> getLabelsByType(LabelType labelType);

	/**
	 * @return Returns the multiple.
	 * @uml.property name="multiple"
	 */
	boolean isMultiple();
}
