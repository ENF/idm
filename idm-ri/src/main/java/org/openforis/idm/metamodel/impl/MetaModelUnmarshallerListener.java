package org.openforis.idm.metamodel.impl;

import java.util.List;

import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.CodingScheme;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.ModelVersion;
import org.openforis.idm.metamodel.Survey;
import org.openforis.idm.metamodel.Unit;

/**
 * @author M. Togna
 * 
 */
public class MetaModelUnmarshallerListener extends Unmarshaller.Listener {

	Survey survey;
	CodeList currentCodeList;
	private int modelVersionPosition = 0;

	@Override
	public void beforeUnmarshal(Object target, Object parent) {
		if (target instanceof Survey) {
			this.survey = (Survey) target;
		} else if (target instanceof CodeList) {
			this.currentCodeList = (CodeList) target;
		} else if (target instanceof ModelVersionImpl) {
			((ModelVersionImpl) target).setPosition(modelVersionPosition++);
		}

		super.beforeUnmarshal(target, parent);
	}

	@Override
	public void afterUnmarshal(Object target, Object parent) {
		if (target instanceof AbstractModelObjectDefinition) {
			AbstractModelObjectDefinition modelObjectDefinition = (AbstractModelObjectDefinition) target;
			this.afterUnmarshallModelObjectDefinition(parent, modelObjectDefinition);
		}

		if (target instanceof CodeListImpl) {
			CodeListImpl codeList = (CodeListImpl) target;
			this.setModelVersions(codeList, codeList.sinceAttribute, codeList.deprecatedAttribute);
		}

		if (target instanceof CodeListItemImpl) {
			CodeListItemImpl codeListItem = (CodeListItemImpl) target;
			this.setModelVersions(codeListItem, codeListItem.sinceAttribute, codeListItem.deprecatedAttribute);
		}

		if (target instanceof CodeDefinitionImpl) {
			CodeDefinitionImpl codeDefinition = (CodeDefinitionImpl) target;
			if (StringUtils.isNotEmpty(codeDefinition.schemeName)) {
				CodingScheme codingScheme = this.getCodingScheme(codeDefinition.schemeName);
				codeDefinition.setCodingScheme(codingScheme);
			}
		}

		if (target instanceof PrecisionImpl) {
			PrecisionImpl precision = (PrecisionImpl) target;
			if (StringUtils.isNotBlank(precision.unitAttribute)) {
				Unit unit = this.getUnit(precision.unitAttribute);
				precision.setUnit(unit);
			}
		}

		if (target instanceof CodeAttributeDefinitionImpl) {
			CodeAttributeDefinitionImpl codeDef = (CodeAttributeDefinitionImpl) target;
			if (StringUtils.isNotEmpty(codeDef.listAttribute)) {
				CodeList codeList = this.getCodeList(codeDef.listAttribute);
				codeDef.setList(codeList);
			}
		}

		super.afterUnmarshal(target, parent);
	}

	/**
	 * @param parent
	 * @param target
	 */
	private void afterUnmarshallModelObjectDefinition(Object parent, AbstractModelObjectDefinition target) {
		this.setModelVersions(target, target.sinceAttribute, target.deprecatedAttribute);

		Integer minCount = target.getMinCount();
		Integer maxCount = target.getMaxCount();
		String requiredExpression = target.getRequiredExpression();

		if (StringUtils.isNotBlank(requiredExpression) || maxCount != null || minCount != null) {
			CardinalityImpl cardinality = new CardinalityImpl();
			if (StringUtils.isNotBlank(requiredExpression)) {
				cardinality.setRequiredExpression(requiredExpression);
			}
			if (minCount != null) {
				cardinality.setMinCount(minCount);
			}
			if (maxCount != null) {
				cardinality.setMaxCount(maxCount);
			}
			target.setCardinality(cardinality);
		}

		if (parent instanceof EntityDefinition) {
			EntityDefinition parentDefinition = (EntityDefinition) parent;
			target.setParentDefinition(parentDefinition);
		}
	}

	private CodeList getCodeList(String listName) {
		List<CodeList> codeLists = this.survey.getCodeLists();
		for (CodeList codeList : codeLists) {
			if (codeList.getName().equals(listName)) {
				return codeList;
			}
		}
		return null;
	}

	private Unit getUnit(String unitName) {
		List<Unit> units = this.survey.getUnits();
		for (Unit unit : units) {
			if (unit.getName().equals(unitName)) {
				return unit;
			}
		}
		return null;
	}

	private void setModelVersions(AbstractVersionable versionable, String since, String deprecated) {
		if (StringUtils.isNotBlank(since)) {
			ModelVersion version = this.getModelVersion(since);
			versionable.setSince(version);
		}
		if (StringUtils.isNotBlank(deprecated)) {
			ModelVersion version = this.getModelVersion(deprecated);
			versionable.setDeprecated(version);
		}
	}

	private CodingScheme getCodingScheme(String name) {
		List<CodingScheme> codingSchemes = this.currentCodeList.getCodingSchemes();
		for (CodingScheme codingScheme : codingSchemes) {
			if (codingScheme.getName().equals(name)) {
				return codingScheme;
			}
		}
		return null;
	}

	/**
	 * Returns a ModelVersion
	 * 
	 * @param name
	 * @return
	 */
	private ModelVersion getModelVersion(String name) {
		List<ModelVersion> versions = this.survey.getVersions();
		for (ModelVersion modelVersion : versions) {
			if (modelVersion.getName().equals(name)) {
				return modelVersion;
			}
		}
		return null;
	}

}
