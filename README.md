# README #

Android Library to build an Android style Form from a JSON object.
The individual form elements can be used independently of the form as well.

### How do I get set up? ###

### Gradle Configuration
* Add the following to your app .gradle 

Dependency 
```
 dependencies {

 compile 'com.giift:formr:2012.  6.09.+'

 }

```
Configure Repository
```
repositories {
    maven {
        url 'https://api.bitbucket.org/1.0/repositories/giiftalldev/giiftsdk-maven/raw/master'
        credentials {
            username getRepositoryUsername()
            password getRepositoryPassword()
        }
    }
    flatDir {
        dirs 'libs'
    }
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

## The different type of fields currently supported in the library are ## 

### 1. Button Choice ###

This field allows users to select from a choice of available options

It can be added through code

```
ButtonChoice button = new ButtonChoice(context)
button.InitOptionsArray(optionsArray) // where optionsArray can be a HashMap<String, String> or ArrayList<Pair<String, String>>
```

You can also add the button choice using xml

```
<com.giift.formr.field.Text
        android:id="@+id/text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```
### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact