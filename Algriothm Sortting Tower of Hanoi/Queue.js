// Xây dựng cho hàng đợi
function Queue() {
    this.list = new LinkedList();
  }
             
  // Enqueue một yếu tố để xếp hàng
  Queue.prototype.enqueue = function(e) {
    this.list.add(e);
  }
        
  // Xóa một phần tử khỏi phần đầu của hàng đợi
  Queue.prototype.dequeue = function() {
    return this.list.removeFirst();
  }
  
  Queue.prototype.getSize = function() {
    return this.list.getSize();
  }
                    
  Queue.prototype.isEmpty = function() {
    return this.list.isEmpty();
  }