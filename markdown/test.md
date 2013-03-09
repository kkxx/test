Markdowm Test
========
Andy (erpingwu@gmail.com)  
2013/03/09

# code
## code and syntax highlight
```cpp
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
```


```python
#-*-coding:utf-8-*-
if __name__ == '__main__':
    print 'hello world!'
```


# Others
- [x] @mentions, #refs, [links](), **formatting**, and <del>tags</del> supported
- [x] list syntax required (any unordered or ordered list supported)
- [x] this is a complete item
- [ ] this is an incomplete item

# References
 
## Markdown  
- [Markdown: Syntax](http://daringfireball.net/projects/markdown/syntax)
- [markdown](http://wowubuntu.com/markdown/ "markdown")
- [GitHub Flavored Markdown](https://help.github.com/articles/github-flavored-markdown)
- [Doxygen Markdown support](http://www.stack.nl/~dimitri/doxygen/manual/markdown.html)

## Pandoc
- [pandoc](http://johnmacfarlane.net/pandoc/)
> pandoc -o test.pdf test.md
> pandoc --toc -o test.pdf test.md

## AsciiArt
* [AsciiArt][1](http://www.network-science.de/ascii/)
* [AsciiArt][2](http://patorjk.com/software/taag/#p=display&f=Bulbhead&t=Piano)

<pre>
 ____  _  ____  _      ____ 
/  __\/ \/  _ \/ \  /|/  _ \
|  \/|| || / \|| |\ ||| / \|
|  __/| || |-||| | \||| \_/|
\_/   \_/\_/ \|\_/  \|\____/
</pre>
          
<pre>
(  _ \(_  _)  /__\  ( \( )(  _  )
 )___/ _)(_  /(__)\  )  (  )(_)(
(__)  (____)(__)(__)(_)\_)(_____)
</pre>


