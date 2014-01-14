OCTAL:	lui $r0, 0
	sw $r3, 1($r0)		#store jr at MEM[1]
	lw $r3, 0($r0)		#r3 = MEM[0]
	lui $r1, 0		#r1 is part of result
	lui $r2, 0
	addi $r2, $r2, 1	#r2 is always 1.  shift by 1 every time.
	
	sllv $r1, $r3, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	
	or $r0, $r0, $r1
	
	sllv $r1, $r3, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	
	or $r0, $r0, $r1
	
	sllv $r1, $r3, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	srlv $r1, $r1, $r2
	
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	sllv $r1, $r1, $r2
	
	or $r0, $r0, $r1
	
	lui $r2, 0
	disp $r0, 0 	#display result in d[0]
	lw $r3, 1($r2)
	
	or $r2, $r2, $r2
	or $r2, $r2, $r2
	or $r2, $r2, $r2
	jr $r3
	
	
	
	