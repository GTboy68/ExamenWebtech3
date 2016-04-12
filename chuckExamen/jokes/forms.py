from django import forms

class NameForm(forms.Form):
    firstname = forms.CharField(label='firstname', max_length=100)
    lastname = forms.CharField(label='lastname', max_length=100)