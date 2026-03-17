package pages;

import org.openqa.selenium.By;

import baseTest.baseTest;

public class home extends baseTest {
	
	private By menuList = By.xpath("//ul[@class='nav navbar-nav']/li/a");//ul[@class='nav navbar-nav']
	private By title = By.id("id_gender1");
	private By name = By.xpath("//input[@placeholder='Name']");
	private By email = By.xpath("//input[@data-qa='signup-email']");
	private By signUpBtn = By.xpath("//button[normalize-space()='Signup']");
	private By pgTitle = By.xpath("//*[@id=\"form\"]/div/div/div/div/h2");
	private By pssword = By.id("password");
	private By days = By.id("days");
	private By months = By.id("months");
	private By yrs = By.id("years");
	private By newsLetter = By.id("newsletter");
	private By firstName = By.id("first_name");
	private By lastName = By.id("last_name");
	private By company = By.name("company");
	private By address1 = By.id("address1");
	private By country = By.id("country");
	private By state = By.id("state");
	private By city = By.id("city");
	private By zipCode = By.id("zipcode");
	private By mobileNo = By.id("mobile_number");
	private By createBtn = By.xpath("//*[@id=\"form\"]/div/div/div/div/form/button");
	private By successMsg = By.xpath("//h2[@class='title text-center']");
	private By continueBtn = By.xpath("//a[normalize-space()='Continue']");
	private By deleteNotification = By.xpath("//b[normalize-space()='Account Deleted!']");
	
	
	public void clickMenuItem() {
		selectMenuItem(menuList,"Signup / Login");
	}
	
	public void signUp() {
		
		type(name, "Test2");
		type(email,"automationEngineer@gmail.com");	
		log.info("Page Title" + getPageTitle());
		click(signUpBtn);
		
	}
	
	public String getPageTitle() {	
		
		String title = getText(pgTitle);
		return title;
		
	}
	
	public void fillForm() {
		
		click(title);
		type(pssword, "@utomation");
		selectByText(days,"16");
		selectByText(months, "June");
		selectByText(yrs,"1971");
		click(newsLetter);
		type(firstName,"Makavaeli");
		type(lastName,"The Don");
		type(company,"The 7 day theory");
		type(address1,"AddOne");
		selectByText(country,"United States");
		type(state,"Los Angeles");
		type(city,"Compton");
		type(zipCode,"5421");
		type(mobileNo,"0712456843");
		scrollAndClick(createBtn);
		click(createBtn);	
		getText(successMsg);
		click(continueBtn);	
		
	}
	
	public void verifyUser() {
		
		isLoggedIn(menuList,"Test2");
	}
	
	public void deleteAc() {
		
       selectMenuItem(menuList,"Delete Account");
       getText(deleteNotification);
       click(continueBtn);	
       
	}
	
}
