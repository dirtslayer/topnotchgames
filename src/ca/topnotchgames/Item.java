package ca.topnotchgames;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.datanucleus.api.jpa.annotations.Extension;


@Entity
public class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5575242952509062767L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true") 
	private Long id;
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true") 
	private String group;    // col 0
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true") 
	private String category; // col 1
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true") 
	private String name; // col 3
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true") 
	private String price; // col 8


	public Item(String category2, String group2, 
			String name2, String price2 ) {
		this.category = category2;
		this.group = group2;
		this.name = name2;
		this.price = price2;

	}

	public String toXML() {
		return "<item><id>" + id + "</id><group>" + group + "</group><category>" + category + "</category><name>" +
				name + "</name><price>" + price + "</price> </item>";
	}

	public Item() {

	}

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}	


	public void setGroup(String s) {
		this.group = s;
	}
	public String getGroup () {
		return this.group ;
	}

	public void setCategory (String s) {
		this.category = s;
	}
	public String getCategory () {
		return this.category  ;
	}

	public void setName(String s) {
		this.name = s;
	}
	public String getName() {
		return this.name ;
	}

	public void setPrice(String s) {
		this.price = s;
	}
	public String getPrice() {
		return this.price ;
	}



}
