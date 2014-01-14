MOD:		lui $r2, 0
		sw $r3, 3($r2)  #store $r3 at MEM[3]
		lui $r3, 0	#r3 resets to 0x0000
		lw $r0, 0($r3)	#$r0 holds MEM[0]
		lw $r1, 1($r3)	#$r1 holds MEM[1]
		slt $r2, $r1, $r3	#if r1 < 0 return 1. r1 > 0, return 0.
		beq $r3, $r2, loop
		#MEM[1] is negative
		sub $r1, $r3, $r1 	# 0+negativeNum = negativeNum
		or $r3, $r3, $r3

loop: 		slt $r2, $r0, $r1	#if MEM[0] < MEM[1], return 1.
		bne $r3, $r2, end	
		sub $r0, $r0, $r1
		j loop

end:		sw $r0, 2($r3)		#store result in MEM[2]	
		lui $r2, 0
		lw $r3, 3($r2)
		
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3
		or $r3, $r3, $r3					

		jr $r3
		
