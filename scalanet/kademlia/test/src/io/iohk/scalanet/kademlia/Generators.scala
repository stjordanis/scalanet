package io.iohk.scalanet.kademlia

import io.iohk.scalanet.kademlia.KRouter.NodeRecord
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import scodec.bits.BitVector

import scala.collection.mutable.ListBuffer
import scala.util.Random

object Generators {

  val defaultBitLength = 16

  def genBitVector(bitLength: Int = defaultBitLength): Gen[BitVector] =
    for {
      bools <- Gen.listOfN(bitLength, arbitrary[Boolean])
    } yield BitVector.bits(bools)

  def genBitVectorPairs(
      bitLength: Int = defaultBitLength
  ): Gen[(BitVector, BitVector)] =
    for {
      v1 <- genBitVector(bitLength)
      v2 <- genBitVector(bitLength)
    } yield (v1, v2)

  def genBitVectorTrips(
      bitLength: Int = defaultBitLength
  ): Gen[(BitVector, BitVector, BitVector)] =
    for {
      v1 <- genBitVector(bitLength)
      v2 <- genBitVector(bitLength)
      v3 <- genBitVector(bitLength)
    } yield (v1, v2, v3)

  def genBitVectorExhaustive(
      bitLength: Int = defaultBitLength
  ): List[BitVector] = {
    def loop(acc: ListBuffer[BitVector], b: BitVector, i: Int, n: Int): Unit = {
      if (i == n) {
        acc.append(b)
      } else {
        loop(acc, b.clear(i), i + 1, n)
        loop(acc, b.set(i), i + 1, n)
      }
    }

    val l = ListBuffer[BitVector]()
    loop(l, BitVector.low(bitLength), 0, bitLength)
    l.toList
  }

  def genBitVectorTripsExhaustive(
      bitLength: Int
  ): List[(BitVector, BitVector, BitVector)] = {
    for {
      x <- genBitVectorExhaustive(bitLength)
      y <- genBitVectorExhaustive(bitLength)
      z <- genBitVectorExhaustive(bitLength)
    } yield (x, y, z)
  }

  def aRandomBitVector(bitLength: Int = defaultBitLength): BitVector =
    BitVector.bits(Range(0, bitLength).map(_ => Random.nextBoolean()))

  def aRandomNodeRecord(
      bitLength: Int = defaultBitLength
  ): NodeRecord[String] = {
    NodeRecord(
      id = aRandomBitVector(bitLength),
      routingAddress = Random.alphanumeric.take(4).mkString,
      messagingAddress = Random.alphanumeric.take(4).mkString
    )
  }
}
