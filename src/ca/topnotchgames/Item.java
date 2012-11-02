package ca.topnotchgames;

public class Item  {
	private String group;    // col 0
	private String category; // col 1
	private String name; // col 3
	private String price; // col 8


	public Item(String group2, 
			String name2, String price2 ) {
		
		this.group = group2;
		this.name = name2;
		this.price = price2;

	}

	public String toXML() {
		return "<item><group>" + group + "</group><name>" +
				name + "</name><price>" + price + "</price> </item>";
	}

	
	public Item() {
		this.category = "";
		this.group = "";
		this.name = "";
		this.price = "";
	}

	public void setGroup(String s) {
		this.group = s;
	}
	public String getGroup () {
		return this.group ;
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
