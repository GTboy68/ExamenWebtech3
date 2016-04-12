from django import forms

class NameForm(forms.Form):
    firstname = forms.CharField(label='Firstname ', max_length=100)
    lastname = forms.CharField(label='Lastname', max_length=100)