<?php
class bidbuttonhelper extends Helper

{
var $output;

 function draw_buttons($row)
	{

		{
		$bids = $row['Bid'];

		foreach ($bids as $bid)
				{
                if($bid['partial']==1)
                {$output .= '<b>Partial bid in place</b>';}

                else
                {$output .= '<b>Complete bid already made</b>';}
				}
		}

	else
	{
	}

	return $output;
	}
}

?>