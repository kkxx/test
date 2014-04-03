- buildout init
```
E:\mydev\test\py\buildout_prj>bin\mytestpy.exe prj\main.py
2.4.1

E:\mydev\test\py\buildout_prj>python prj\main.py
3.0.1
```

- buildout.cfg
```
[buildout]
index = http://pypi.douban.com/simple
parts = mytest
develop = .
eggs = tornado

find-links = 
  http://download.zope.org/ppix

versions = versions

[versions]
tornado = 2.4.1

[mytest]
recipe = zc.recipe.egg
interpreter = mytestpy
eggs = ${buildout:eggs}

```

- setup.py
```
from setuptools import setup, find_packages
setup(
    name = 'mytest',
    package_dir = {'':'mytest'},
    install_requires = ['setuptools'],
)
```


-  wget http://svn.zope.org/*checkout*/zc.buildout/trunk/bootstrap/bootstrap.py