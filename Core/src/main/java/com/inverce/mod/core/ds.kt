package com.inverce.mod.core

import com.inverce.mod.core.configuration.ReadOnlyValue
import com.inverce.mod.core.configuration.shared.SharedIntValue



var kanda:  Int by SharedIntValue("dsds")



fun ds() {
    kanda = 33

}