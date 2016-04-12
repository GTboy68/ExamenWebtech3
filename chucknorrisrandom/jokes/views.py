from django.shortcuts import render
from django.http import HttpResponse, Http404
from django.template import RequestContext, loader
from django.views import generic
from .forms import NameForm
import redis, json, urllib, urllib2
from django.shortcuts import get_object_or_404, redirect, render

r = redis.StrictRedis(host='localhost', port=6379, db=0)
url = 'http://api.icndb.com/jokes/random'


def fill_db():
    r.set(1, '{ "id": 1, "joke": "Chuck Norris uses ribbed condoms inside out, so he gets the pleasure.", "categories": ["explicit"] }')

fill_db()

def index(request):
	joke_list = r.keys()
	jokes = []
	for j in joke_list:
		jokeredis = (r.get(j))
		jokejson = json.loads(jokeredis)
		jokes.append(jokejson)
	return render(request,'jokes/index.html',{'jokes':jokes})

def randomname(request):
    # if this is a POST request we need to process the form data
    if request.method == 'POST':
        # create a form instance and populate it with data from the request:
        form = NameForm(request.POST)
        # check whether it's valid:
        if form.is_valid():
            joke = {}
            firstname = form.cleaned_data['firstname']
            lastname = form.cleaned_data['lastname']
            response = urllib2.urlopen(url + '?firstName=' + firstname + '&lastName=' + lastname)
            joke = json.load(response)
            if (joke['type'] != 'success'):
                joke = {}
                oke['joke'] = 'Joke not found'
                joke['categories'] ='operation failed'
            else:
                jokejson = joke['value']
                joke_id = jokejson['id']
                if (r.get(joke_id) is None):
                    r.set(joke_id, json.dumps(joke['value']))
                    joke = json.loads(r.get(joke_id))
                else:
                    joke = json.loads(r.get(joke_id))

            # ...
            # redirect to a new URL:

            return render(request, 'jokes/random.html', {'joke': joke})

    # if a GET (or any other method) we'll create a blank form
    else:
        form = NameForm()

    return render(request, 'jokes/name.html', {'form': form})

def randomnameresponse(request):
    return render(request, 'jokes/random.html', {'joke': joke})

def random(request):
    joke = {}
    response = urllib2.urlopen(url)
    joke = json.load(response)
    if (joke['type'] != 'success'):
        joke = {}
        joke['joke'] = 'Joke not found'
        joke['categories'] ='operation failed'
    else:
        jokejson = joke['value']
        joke_id = jokejson['id']
        if (r.get(joke_id) is None):
            r.set(joke_id, json.dumps(joke['value']))
            joke = json.loads(r.get(joke_id))
        else:
            joke = json.loads(r.get(joke_id))
    return render(request, 'jokes/random.html', {'joke': joke})