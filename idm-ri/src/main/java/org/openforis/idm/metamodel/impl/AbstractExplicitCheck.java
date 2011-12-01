/**
 * 
 */
package org.openforis.idm.metamodel.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openforis.idm.metamodel.ExplicitCheck;
import org.openforis.idm.metamodel.LanguageSpecificText;

/**
 * @author M. Togna
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class AbstractExplicitCheck extends AbstractCheck implements ExplicitCheck {

	@XmlAttribute(name = "flag")
	@XmlJavaTypeAdapter(value = FlagAdapter.class)
	private Flag flag;

	@XmlAttribute(name = "if")
	private String condition;

	@XmlElement(name = "message", type = LanguageSpecificTextImpl.class)
	List<LanguageSpecificText> messages;

	@Override
	public Flag getFlag() {
		return this.flag;
	}

	@Override
	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	@Override
	public String getCondition() {
		return this.condition;
	}

	@Override
	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public List<LanguageSpecificText> getMessages() {
		return this.messages;
	}

	@Override
	public void setMessages(List<LanguageSpecificText> messages) {
		this.messages = messages;
	}

	private static class FlagAdapter extends XmlAdapter<String, Flag> {

		@Override
		public Flag unmarshal(String v) throws Exception {
			return Flag.valueOf(v.toUpperCase());
		}

		@Override
		public String marshal(Flag v) throws Exception {
			return v.toString().toLowerCase();
		}

	}

}
