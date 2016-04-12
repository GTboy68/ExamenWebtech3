from django.conf.urls import include, url
from . import views

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^randomname', views.randomname, name='randomname'),
    url(r'^random', views.random, name='random'),

]