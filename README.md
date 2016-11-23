# README #

Android Library to build an Android style Form from a JSON object.
The individual form elements can be used independently of the form as well.

### How do I get set up? ###

### Gradle Configuration
* Add the following to your app .gradle 

Dependency 
```
 dependencies {

 compile 'com.giift:formr:2016.11.+'

 }

```

The form is capable of handling orientation changes on its own and the values in the form are retained.

The form can be build via code 
```
Form form = new Form(Context);
```

or Added in xml

```
 <com.giift.formr.Form
            android:id="@+id/stripe_form"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
```
To Initialise the form

```
form.Init(json) //example json below
```
Example JSON Configuration in-order to build a Form
```
{
	"fields": {
		"button_choice": {
			"label": "Button Choice Label",
			//Thisisthelabeldisplayedfortheelement"label_id": "",
			"label_replace": [],
			"id": "button_choice",
			"type": "buttonchoice",
			//typeoffieldtobebuilte.gifvaluewastextaTextfieldwouldbeshown"mandatory": false,
			"value": "fifty",
			"group": null,
			"order": 1,
			"settings": {
				"options": {
					"five": "$ 5",
					//Hereweconfiguretheoptionsforbuttonchoice"ten": "$ 10",
					"fifty": "$ 50"
				},
				"readonly": true
			}
		},
		"text": {
			"label": "Standard Text",
			"label_id": "",
			"label_replace": [],
			"id": "text",
			"type": "text",
			"mandatory": true,
			"value": null,
			"group": null,
			"order": 21,
			"settings": {
				"error": {
					"label": "Invalid Text Server Error" // error to be shown on the field
				}"callback": {
					"on_focus_lost": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback", //action to perform on losing focus in this field; the action can be updating the field value or adding a new field below this field etc;  
					"on_value_changed": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback"
				}
			}
		}
	}
}
```

## Input types ## 

Each of the below mentioned Fields has the following method

```
public void SetLabel(String label)
```

Sets a label above the Field.
For Input type Text and TextArea it sets a floating action label

```
public void SetError(@Nullable String error)
```

Sets error with the msg on the Field.

```
public void SetHint(String hint)
```

Sets a hint below the field.


### 1. Button Choice ###

This field allows users to select from a choice of available options

It can be added through code

```
ButtonChoice button = new ButtonChoice(context)
button.InitOptionsArray(optionsArray) // where optionsArray can be a HashMap<String, String> or ArrayList<Pair<String, String>>
```

You can also add the button choice using xml

```
<com.giift.formr.field.ButtonChoice
        android:id="@+id/buttonChoice"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:label= " Button Choice Title "  // Title for the Button options
/>
```

```
 public void SetSelectedPosition(int position)
```
Selects option at specified position


### 2. Card Number ###
This field allows users to input a credit card number . It also has a button to scan credit card to fetch details.

It can be added through code

```
CardNumber button = new CardNumber (context)
```

You can also add the button choice using xml

```
<com.giift.formr.field.CardNumber
        android:id="@+id/card Number"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:label= " Card Number Title "  // Title for the Button options
/>
```

```
public boolean Validate()
```
returns false if invalid credit card number

### 3. Checkbox ###
This adds multi- select checkbox options

### 4. Cvv ###
Input field for collecting credit card cvv details

### 5. Date ###
Field to launch DatePicker Dialog.
```
public void SetDate(java.util.Date date)
```
Set a date to be selected

```
 public java.util.Date GetDate()
```
Get selected date


### 6. Day ###
Input field for entering day of the month

### 7. Dropdown ###
Provides a list of options.
 
### 8. Email ###
Input field for entering a email address

```
boolean Validate()  
```
validates if email address is valid

### 9. Month ###

Provides a drop-down for selecting a month

### 10. Number ###
Input field for entering a number

### 11. Onoff ###

Switch

### 12. Password ###
Input field for entering a password. The input text is encrypted while displaying

### 13. PersonName ###
Input field for entering a name.

### 14. Phone ###
Input field for entering a phone number.

### 15. PostalAddress ###
Input field for entering a address.

### 16. Slider ###
Slider to select a value.

```
public void SetMin(double v)
```
Set minimum value for slider

```
public void SetMax(double v)
```
Set maximum value for slider

```
public void SetStep(double v)
```
Set slider increment value

```
public void SetValue(double v)
```
Set slider selected value

### 17. Text ###

Material style input textbox. 

```
public void SetText(String text)
public void SetText(CharSequence text)
```
Set text value

```
public void SetScannable(boolean scannable)
```
If set to true; a barcode scanner option is displayed


### 18. TextArea ###

Multi line text input

### 19. Url ###
Input field for entering a url

### 20. Year###
Input field for entering a year number

### 21. Zip ###
Input field for entering a zipcode

### Who do I talk to? ###

* If you have any questions please contact us at [vieony.dacosta@giift.com](mailto:vieony.dacosta@giift.com) or [nicolas.lagier@giift.com](mailto:nicolas.lagier@giift.com)