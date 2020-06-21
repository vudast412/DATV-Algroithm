// Trình xây dựng cho ! danh sách
function LinkedList() {
    this.size = 0;
    this.head = null;
    this.tail = null;
  }
        
  // Trình xây dựng cho nút
  function Node(e) {
    this.element = e;
    this.next = null;
  }
        
  LinkedList.prototype.add = function(e) {
    // Tạo một nút mới cho phần tử e
    var node = new Node(e);
          
    if (this.tail == null) {
      this.head = this.tail = node;
    }
    else {
      this.tail.next = node;
      this.tail = node;
    }
   
    this.size++;
  }
        
  // Chèn phần tử vào đầu danh sách
  LinkedList.prototype.addFirst = function(e) {
    var node = new Node(e); // Tạo một nút mới
    node.next = this.head;
    this.head = node;
    this.size++;
          
    if (this.tail == null) {
      this.tail = this.head;
    }
  }
  
  // Chèn phần tử vào chỉ mục đã chỉ định
  LinkedList.prototype.insert = function(index, e) {
    if (index == 0) this.addFirst(e);
    else if (index >= this.size) this.add(e);
    else {
      current = this.head;
      for (var i = 1; i < index; i++) {
        current = current.next;
      }
      temp = current.next;
      current.next = new Node(e); // Tạo một nút mới
      (current.next).next = temp;
      this.size++;
    }
  }
        
  // Xóa phần tử đầu tiên trong danh sách
  LinkedList.prototype.removeFirst = function() {
    if (this.size == 0) return null;
    else {
      temp = this.head;
      this.head = this.head.next;
      this.size--;
      if (this.head == null) this.tail = null;
      return temp.element;
    }
  }
        
  // Xóa phần tử cuối cùng trong danh sách
  LinkedList.prototype.removeLast = function() {
    if (this.size == 0) return null;
    else if (this.size == 1) {
      temp = this.head;
      this.head = this.tail = null;
      this.size--;
      return temp.element;
    }
    else {
      current = this.head;
      while (current.next != this.tail) {
        current = current.next;
      }
      temp = this.tail;
      this.tail = current;
      this.tail.next = null;
      this.size--;
      return temp.element;
    }
  }
    
  // Xóa phần tử đã chỉ định
  LinkedList.prototype.remove = function(e) {
    var index = this.indexOf(e);
    if (index < 0) {
      return false;
    }
    else {
      this.removeAt(index);
      return true;
    }
  }
  
  // Xóa phần tử tại chỉ mục đã chỉ định
  LinkedList.prototype.removeAt = function(index) {
    if (index < 0 || index >= this.size) {
      return null; // ngoài phạm vi sẽ out
    } 
    else if (index == 0) return this.removeFirst();
    else if (index == this.size - 1) return this.removeLast();
    else {
      previous = this.head;
      for (var i = 1; i < index; i++) {
        previous = previous.next;
      }
      current = previous.next;
      previous.next = current.next;
      this.size--;
      return current.element;
    }
  }
        
  LinkedList.prototype.getSize = function() {
    return this.size;
  }
              
  LinkedList.prototype.toString = function() {
    // Tạo một nút mới cho phần tử e
    var s = "[";
    var current = this.head;
    while (current !== null) {
      s += (current === this.head ? "" : ", ") + current.element;   
      current = current.next;
    } 
       
    return s + "]";
  }
        
  LinkedList.prototype.clear = function() {
    this.head = this.tail = null;            
    this.size = 0;
  }
        
  LinkedList.prototype.isEmpty = function() {
    return this.size == 0;
  }
  
  LinkedList.prototype.indexOf = function(e) {
    current = this.head;
    var index = 0;
    while (current != null && current.element !== e) {
      current = current.next;
      index++;
    }
    
    if (index >= this.getSize())
      return -1;
    else
      return index;
  }
        
  LinkedList.prototype.get = function(index) {
    if (index < 0 || index > this.size - 1)
      return null;
         
    current = this.head;
    for (var i = 0; i < index; i++) {
      current = current.next;
    }
    return current.element; 
  }
        
  LinkedList.prototype.lastIndexOf = function(e) {
    return null; // Để được thực hiện
  }
  
  LinkedList.prototype.contains = function(e) {
    return this.indexOf(e) >= 0; // Để được thực hiện
  }