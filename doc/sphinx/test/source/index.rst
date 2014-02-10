.. test documentation master file, created by
   sphinx-quickstart on Mon Feb 10 19:53:41 2014.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.
  
Sphinx Demo
================================

测试sphinx

.. toctree::
   :maxdepth: 2


文档标题
==================

子标题
------------------

子子标题
^^^^^^^^^^^^^^^^^^

安装
------------------

- **easy_install sphinx==1.2.1**

- **pip install sphinx_bootstrap_theme -U -i http://pypi.douban.com/simple**

  - Edit the "conf.py" configuration file to point to the bootstrap theme:
  
    .. code-block:: python
    
      # At the top.
      import sphinx_bootstrap_theme
    
    .. code-block:: python
    
      # html_theme = 'default'
      html_theme = 'bootstrap'
      html_theme_path = sphinx_bootstrap_theme.get_html_theme_path()

图片
------------------
.. image:: _static/sphinxheader.png

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

