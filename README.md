# README #

Android Library to build an Android style Form from a JSON object.
The individual form elements can be used independently of the form as well.

### How do I get set up? ###

###  Gradle Configuration ### 
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
		"giift_slider": {
			"label": "Select Amount to transfer",
			"label_id": "",
			"label_replace": [],
			"id": "giift_slider",
			"type": "slider",
			"mandatory": false,
			"value": 150000,
			"group": null,
			"order": 0,
			"settings": {
				"min": 10000,
				"max": 5000000,
				"step": 1,
				"unit": "miles",
				"fx": {
					"unit": "usd",
					"a": {
						"type": "stepped",
						"params": [{
							"value": 0.0014,
							"min": 10000,
							"max": 19999
						},
						{
							"min": 20000,
							"max": 21999,
							"value": 0.0016
						},
						{
							"min": 22000,
							"max": 49999,
							"value": 0.0041
						},
						{
							"min": 50000,
							"max": 99999,
							"value": 0.00575
						},
						{
							"min": 100000,
							"max": 119999,
							"value": 0.0061
						},
						{
							"min": 120000,
							"max": 399999,
							"value": 0.0075
						},
						{
							"min": 400000,
							"max": 5000000,
							"value": 0.0081
						}]
					},
					"b": {
						"type": "fixed",
						"params": {
							"value": 0
						}
					}
				},
				"hint": {
					"label": "Slider hint"
				},
				"error": {
					"label": "Invalid Position"
				}
			}
		},
		"giift_slider_readonly": {
			"label": "giift_slider_readonly",
			"label_id": "",
			"label_replace": [],
			"id": "giift_slider_readonly",
			"type": "slider",
			"mandatory": false,
			"value": 150000,
			"group": null,
			"order": 0,
			"settings": {
				"min": 10000,
				"max": 5000000,
				"step": 1,
				"unit": "miles",
				"fx": {
					"unit": "usd",
					"a": {
						"type": "stepped",
						"params": [{
							"value": 0.0014,
							"min": 10000,
							"max": 19999
						},
						{
							"min": 20000,
							"max": 21999,
							"value": 0.0016
						},
						{
							"min": 22000,
							"max": 49999,
							"value": 0.0041
						},
						{
							"min": 50000,
							"max": 99999,
							"value": 0.00575
						},
						{
							"min": 100000,
							"max": 119999,
							"value": 0.0061
						},
						{
							"min": 120000,
							"max": 399999,
							"value": 0.0075
						},
						{
							"min": 400000,
							"max": 5000000,
							"value": 0.0081
						}]
					},
					"b": {
						"type": "fixed",
						"params": {
							"value": 0
						}
					}
				},
				"readonly": true
			}
		},
		"button_choice": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "button_choice",
			"type": "buttonchoice",
			"mandatory": true,
			"value": "ten",
			"group": null,
			"order": 1,
			"settings": {
				"options": {
					"five": "$ 5",
					"ten": "$ 10",
					"fifty": "$ 50"
				},
				"hint": {
					"label": "Button Choice hint"
				},
				"error": {
					"label": "Invalid choice"
				}
			}
		},
		"button_choice_read_only": {
			"label": "button_choice_read_only",
			"label_id": "",
			"label_replace": [],
			"id": "button_choice_read_only",
			"type": "buttonchoice",
			"mandatory": false,
			"value": "fifty",
			"group": null,
			"order": 1,
			"settings": {
				"options": {
					"five": "$ 5",
					"ten": "$ 10",
					"fifty": "$ 50"
				},
				"readonly": true
			}
		},
		"card_number_readonly": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "card_number_readonly",
			"type": "cardnumber",
			"mandatory": false,
			"value": "4012111111111111",
			"group": null,
			"order": 3,
			"settings": {
				"validation": ["luhn"],
				"readonly": true
			}
		},
		"ccv": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "ccv",
			"type": "ccv",
			"mandatory": true,
			"value": "123",
			"group": null,
			"order": 4,
			"settings": {
				"validation": {
					"lengthmin": 3,
					"lengthmax": 4,
					"0": "numeric"
				},
				"hint": {
					"label": "Ccv hint"
				},
				"readonly": true
			}
		},
		"check_box": {
			"label": "Delivery date",
			"label_id": "",
			"label_replace": [],
			"id": "check_box",
			"type": "checkbox",
			"mandatory": true,
			"value": null,
			"group": null,
			"order": 5,
			"settings": {
				"options": {
					"1": "Monday",
					"2": "Tuesday",
					"3": "Wednesday"
				},
				"hint": {
					"label": "Checkbox hint"
				},
				"error": {
					"label": "Checkbox Error"
				}
			}
		},
		"check_box": {
			"label": "Delivery date Read Only",
			"label_id": "",
			"label_replace": [],
			"id": "check_box",
			"type": "checkbox",
			"mandatory": true,
			"value": null,
			"group": null,
			"order": 5,
			"settings": {
				"options": {
					"1": "Monday",
					"2": "Tuesday",
					"3": "Wednesday"
				},
				"readonly": true
			}
		},
		"date": {
			"label": "Date",
			"label_id": "",
			"label_replace": [],
			"id": "date",
			"type": "date",
			"mandatory": true,
			"value": null,
			"group": null,
			"order": 6,
			"settings": {
				"hint": {
					"label": "Date hint"
				},
				"error": {
					"label": "Date Error"
				}
			}
		},
		"date_read_only": {
			"label": "Date Read Only",
			"label_id": "",
			"label_replace": [],
			"id": "date_read_only",
			"type": "date",
			"mandatory": false,
			"value": "2016-04-12",
			"group": null,
			"order": 6,
			"settings": {
				"readonly": true
			}
		},
		"date_with_start_end_date": {
			"label": "Date with start and end dates",
			"label_id": "",
			"label_replace": [],
			"id": "date_with_start_end_date",
			"type": "date",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 7,
			"settings": {
				"start_date": "2015-08-24",
				"end_date": "2016-08-24"
			}
		},
		"day": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "day",
			"type": "day",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 8,
			"settings": {
				"validation": {
					"valuemax": 31,
					"valuemin": 1
				},
				"options": {
					"1": "01",
					"2": "02",
					"3": "03",
					"4": "04",
					"5": "05",
					"6": "06",
					"7": "07",
					"8": "08",
					"9": "09",
					"10": "10",
					"11": "11",
					"12": "12",
					"13": "13",
					"14": "14",
					"15": "15",
					"16": "16",
					"17": "17",
					"18": "18",
					"19": "19",
					"20": "20",
					"21": "21",
					"22": "22",
					"23": "23",
					"24": "24",
					"25": "25",
					"26": "26",
					"27": "27",
					"28": "28",
					"29": "29",
					"30": "30",
					"31": "31"
				}
			}
		},
		"drop_down": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "drop_down",
			"type": "dropdown",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 9,
			"settings": []
		},
		"dropdown_readonly": {
			"label": "dropdown_readonly",
			"label_id": "",
			"label_replace": [],
			"id": "dropdown_readonly",
			"type": "dropdown",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 32,
			"settings": {
				"options": {
					"FR": "France",
					"KR": "Korea",
					"US": "United States"
				},
				"readonly": true
			}
		},
		"email": {
			"label": "email_readonly",
			"label_id": "",
			"label_replace": [],
			"id": "email",
			"type": "email",
			"mandatory": false,
			"value": "vieony.dacosta@giift.com",
			"group": null,
			"order": 10,
			"settings": {
				"validation": ["email"],
				"readonly": true
			}
		},
		"hidden": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "hidden",
			"type": "hidden",
			"mandatory": false,
			"value": "1459478727MVpMX",
			"group": null,
			"order": 11,
			"settings": []
		},
		"month": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "month",
			"type": "month",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 12,
			"settings": {
				"validation": {
					"valuemax": 12,
					"valuemin": 1
				},
				"options": {
					"1": "01",
					"2": "02",
					"3": "03",
					"4": "04",
					"5": "05",
					"6": "06",
					"7": "07",
					"8": "08",
					"9": "09",
					"10": "10",
					"11": "11",
					"12": "12"
				},
				"error": {
					"label": "Invalid Month"
				}
			}
		},
		"month_read_only": {
			"label": "month_read_only",
			"label_id": "",
			"label_replace": [],
			"id": "month_read_only",
			"type": "month",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 12,
			"settings": {
				"validation": {
					"valuemax": 12,
					"valuemin": 1
				},
				"options": {
					"1": "01",
					"2": "02",
					"3": "03",
					"4": "04",
					"5": "05",
					"6": "06",
					"7": "07",
					"8": "08",
					"9": "09",
					"10": "10",
					"11": "11",
					"12": "12"
				},
				"readonly": true
			}
		},
		"number": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "number",
			"type": "number",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 13,
			"settings": {
				"validation": ["numeric"]
			}
		},
		"on_off": {
			"label": "on_off_readonly",
			"label_id": "",
			"label_replace": [],
			"id": "on_off",
			"type": "onoff",
			"mandatory": false,
			"value": true,
			"group": null,
			"order": 14,
			"settings": {
				"readonly": true
			}
		},
		"password": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "password",
			"type": "password",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 15,
			"settings": {
				"error": {
					"label": "Invalid password",
					"label_id": "invalid_password",
					"replace": []
				}
			}
		},
		"person_name": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "person_name",
			"type": "personname",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 16,
			"settings": []
		},
		"phone": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "phone",
			"type": "phone",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 17,
			"settings": []
		},
		"postal_address": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "postal_address",
			"type": "postaladdress",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 18,
			"settings": []
		},
		"radio": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "radio",
			"type": "radio",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 19,
			"settings": []
		},
		"separator": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "separator",
			"type": "separator",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 20,
			"settings": []
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
					"label": "Invalid Text Server Error"
				}
			}
		},
		"scannable_text": {
			"label": "Scannable Text",
			"label_id": "",
			"label_replace": [],
			"id": "scannable_text",
			"type": "text",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 22,
			"settings": {
				"scannable": "true",
				"hint": {
					"label": "Scannable text hint"
				}
			}
		},
		"upload": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "upload",
			"type": "upload",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 23,
			"settings": []
		},
		"url": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "url",
			"type": "url",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 24,
			"settings": []
		},
		"year": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "year",
			"type": "year",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 25,
			"settings": {
				"validation": {
					"length": 4
				}
			}
		},
		"zip": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "zip",
			"type": "zip",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 26,
			"settings": []
		},
		"text_area": {
			"label": "Message to your friend",
			"label_id": "",
			"label_replace": [],
			"id": "text_area",
			"type": "textarea",
			"mandatory": true,
			"value": null,
			"group": null,
			"order": 27,
			"settings": {
				"readonly": true
			}
		},
		"text_area_with_callback": {
			"label": "Message to call back your friend",
			"label_id": "",
			"label_replace": [],
			"id": "text_area_with_callback",
			"type": "textarea",
			"mandatory": true,
			"value": null,
			"group": null,
			"order": 28,
			"settings": {
				"callback": {
					"on_focus_lost": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback",
					"on_value_changed": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback"
				},
				"hint": {
					"label": "TextArea hint"
				}
			}
		},
		"card_number": {
			"label": "",
			"label_id": "",
			"label_replace": [],
			"id": "card_number",
			"type": "cardnumber",
			"mandatory": true,
			"value": null,
			"group": null,
			"order": 2,
			"settings": {
				"validation": ["luhn"],
				"hint": {
					"label": "Card Number hint"
				},
				"error": {
					"label": "Card Number Error"
				}
			}
		},
		"email_with_callback": {
			"label": "email_with_callback",
			"label_id": "",
			"label_replace": [],
			"id": "email_with_callback",
			"type": "email",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 30,
			"settings": {
				"validation": ["email"],
				"callback": {
					"on_focus_lost": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback",
					"on_value_changed": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback"
				},
				"hint": {
					"label": "Email hint"
				},
				"error": {
					"label": "Invalid Email Server Error"
				}
			}
		},
		"checkbox_with_callback": {
			"label": "checkbox_with_callback",
			"label_id": "",
			"label_replace": [],
			"id": "checkbox_with_callback",
			"type": "checkbox",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 31,
			"settings": {
				"options": {
					"1": "Monday",
					"2": "Tuesday",
					"3": "Wednesday"
				},
				"callback": {
					"on_focus_lost": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback",
					"on_value_changed": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback"
				}
			}
		},
		"dropdown_with_callback": {
			"label": "dropdown_with_callback",
			"label_id": "",
			"label_replace": [],
			"id": "dropdown_with_callback",
			"type": "dropdown",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 32,
			"settings": {
				"callback": {
					"on_focus_lost": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback",
					"on_value_changed": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback"
				},
				"options": {
					"FR": "France",
					"KR": "Korea",
					"US": "United States"
				},
				"hint": {
					"label": "Dropdown hint"
				},
				"error": {
					"label": "Dropdown Error"
				}
			}
		},
		"month_with_callback": {
			"label": "month_with_callback",
			"label_id": "",
			"label_replace": [],
			"id": "month_with_callback",
			"type": "month",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 33,
			"settings": {
				"validation": {
					"valuemax": 12,
					"valuemin": 1
				},
				"options": {
					"1": "01",
					"2": "02",
					"3": "03",
					"4": "04",
					"5": "05",
					"6": "06",
					"7": "07",
					"8": "08",
					"9": "09",
					"10": "10",
					"11": "11",
					"12": "12"
				},
				"callback": {
					"on_focus_lost": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback",
					"on_value_changed": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback"
				},
				"hint": {
					"label": "Month hint"
				}
			}
		},
		"onoff_with_callback": {
			"label": "onoff_with_callback",
			"label_id": "",
			"label_replace": [],
			"id": "onoff_with_callback",
			"type": "onoff",
			"mandatory": true,
			"value": true,
			"group": null,
			"order": 34,
			"settings": {
				"callback": {
					"on_focus_lost": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback",
					"on_value_changed": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback"
				},
				"hint": {
					"label": "Onoff hint"
				},
				"error": {
					"label": "Invalid Setting"
				}
			}
		},
		"text_with_callback": {
			"label": "text_with_callback",
			"label_id": "",
			"label_replace": [],
			"id": "text_with_callback",
			"type": "text",
			"mandatory": false,
			"value": null,
			"group": null,
			"order": 29,
			"settings": {
				"callback": {
					"on_focus_lost": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback",
					"on_value_changed": "http:\/\/dev.giift.com\/uat\/public\/api\/test\/formr\/callback"
				}
			}
		}
	},
	"groups": [],
	"settings": []
}
```

##  The different type of fields currently supported in the library are  ## 

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