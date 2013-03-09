Markdowm Test
========
Andy (erpingwu@gmail.com)
<br/>2013/03/09


# 1 code
## 1.1 single line
`void main()`

## 1.2 multiple line
four-space indents

	/* Create a new heap.  size is automatically rounded up to a multiple
	   of the page size. */
	
	static heap_info *
	internal_function
	new_heap(size_t size, size_t top_pad)
	{
	  size_t page_mask = GLRO(dl_pagesize) - 1; 
	  char *p1, *p2;
	  unsigned long ul;
	  heap_info *h;



<pre>
#-*-coding:utf-8-*-
if __name__ == '__main__':
    print 'hello world!'
</pre>

## 1.3 syntax highlight?
```python
#-*-coding:utf-8-*-
if __name__ == '__main__':
    print 'hello world!'
```

#2 Lists
## 2.1 1. + space
	1. 1
	2. 2
	3. 3
## 2.2 * + space
	* 1
	* 2
	* 3
	- 1
	- 2
	- 3

# Others
- [x] @mentions, #refs, [links](), **formatting**, and <del>tags</del> supported
- [x] list syntax required (any unordered or ordered list supported)
- [x] this is a complete item
- [ ] this is an incomplete item

# References
>http://github.github.com/github-flavored-markdown/sample_content.html
>http://daringfireball.net/projects/markdown/syntax
>http://www.stack.nl/~dimitri/doxygen/manual/markdown.html

>http://www.network-science.de/ascii/
>http://patorjk.com/software/taag/#p=display&f=Bulbhead&t=Piano

<pre>
First Header  | Second Header
------------- | -------------
Content Cell  | Content Cell 
Content Cell  | Content Cell 
</pre>

<pre>
 ____  _  ____  _      ____ 
/  __\/ \/  _ \/ \  /|/  _ \
|  \/|| || / \|| |\ ||| / \|
|  __/| || |-||| | \||| \_/|
\_/   \_/\_/ \|\_/  \|\____/
</pre>
          
<pre>
 ___   _    __    _      ___
| |_) | |  / /\  | |\ | / / \
|_|   |_| /_/--\ |_| \| \_\_/
</pre>
<pre>
(  _ \(_  _)  /__\  ( \( )(  _  )
 )___/ _)(_  /(__)\  )  (  )(_)(
(__)  (____)(__)(__)(_)\_)(_____)
</pre>

<pre>
      _)
 __ \  |  _` | __ \   _ \
 |   | | (   | |   | (   |
 .__/ _|\__,_|_|  _|\___/
_|
</pre>