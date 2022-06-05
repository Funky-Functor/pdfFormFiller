package com.funkyFunctor.pdfFormFiller

import zio.ZIO

import java.io.InputStream

object TestUtilities {
  def loadFromClasspath(path: String): ZIO[Any, Throwable, InputStream] = {
    ZIO(getClass.getClassLoader.getResourceAsStream(path))
  }
}
