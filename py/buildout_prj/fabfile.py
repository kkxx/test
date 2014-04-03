#!/usr/bin/env python
#-*-coding:utf-8-*-

from fabric.api import *
env.warn_only =True

def build():
    local('''mkdir -p mytest; 
        buildout
    ''')
    
def test():
    local(r"bin\mytestpy prj\main.py")
    local(r"python prj\main.py")